package com.sweber.tennis.wiki_import;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class WikiFetcher {
    private final HttpClient httpClient;
    private final ExecutorService fetchPool;
    private final int perHostLimit;
    private final ConcurrentHashMap<String, Semaphore> hostSemaphores = new ConcurrentHashMap<>();

    public WikiFetcher(int globalConcurrency, int perHostLimit) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.fetchPool = Executors.newFixedThreadPool(globalConcurrency);
        this.perHostLimit = perHostLimit;
    }

    private Semaphore semaphoreForHost(String hostname) {
        return hostSemaphores.computeIfAbsent(hostname, k -> new Semaphore(perHostLimit));
    }

    public void shutdown() {
        fetchPool.shutdown();
    }

    public void fetchAndProcessAll(List<WikiPage> pages, Consumer<WikiPageResult> resultConsumer) throws InterruptedException {
        CompletionService<WikiPageResult> cs = new ExecutorCompletionService<>(fetchPool);
        AtomicInteger submitted = new AtomicInteger(0);

        for (WikiPage wp : pages) {
            cs.submit(() -> {
                String url = wp.getUrl();
                URI uri = URI.create(url);
                Semaphore sem = semaphoreForHost(uri.getHost());
                boolean acquired = false;
                try {
                    sem.acquire();
                    acquired = true;
                    WikiPageResult r = fetchWithRetriesAndParse(wp);
                    return r;
                } finally {
                    if (acquired) sem.release();
                }
            });
            submitted.incrementAndGet();
        }

        for (int i = 0; i < submitted.get(); i++) {
            try {
                Future<WikiPageResult> f = cs.take();
                WikiPageResult r = f.get();
                resultConsumer.accept(r);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private WikiPageResult fetchWithRetriesAndParse(WikiPage wp) {
        String url = wp.getUrl();
        int attempts = 0;
        long backoff = 500;
        while (attempts < 3) {
            attempts++;
            try {
                HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                        .timeout(Duration.ofSeconds(20))
                        .header("User-Agent", "TennisAppBot/1.0 (+https://your.app/)")
                        .GET()
                        .build();

                HttpResponse<InputStream> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());
                if (resp.statusCode() != 200) {
                    throw new RuntimeException("Non-200: " + resp.statusCode() + " for " + url);
                }

                try (InputStream is = resp.body()) {
                    Document doc = Jsoup.parse(is, null, url);
                    StringBuilder sb = wp.processWikiDocument(doc);
                    return new WikiPageResult(wp, sb.toString(), null);
                }
            } catch (Exception ex) {
                if (attempts >= 3) {
                    return new WikiPageResult(wp, null, ex);
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return new WikiPageResult(wp, null, ie);
                }
                backoff *= 2;
            }
        }
        return new WikiPageResult(wp, null, new IllegalStateException("Unexpected failure"));
    }

    public static class WikiPageResult {
        public final WikiPage page;
        public final String content;
        public final Exception error;

        public WikiPageResult(WikiPage page, String content, Exception error) {
            this.page = page;
            this.content = content;
            this.error = error;
        }
    }
}

