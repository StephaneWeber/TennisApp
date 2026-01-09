# TennisApp
Helping tweak profiles for the Tennis Clash game

IN DEV
*

TODO
* Refresh players list when updating maxLevel restriction
* Take into account players as possible items to upgrade

Resources:
* https://tennis-clash.fandom.com/wiki/Tennis_Clash_Wiki
* https://felix-halim.github.io/tennis-clash/

Configuration
-------------
This app exposes the following configurable properties (set in `application.yml`, environment variables or other Spring Boot property sources):

- application.csv.players (default: `./data/players.csv`)
  - Path to the players CSV file. Can be a classpath resource (prefix with `classpath:`) or an absolute/relative filesystem path.

- application.csv.ownedPlayers (default: `./data/owned_players.csv`)
  - Path to the owned players CSV file. Used to determine owned player levels.

- application.csv.gear (default: `./data/gear.csv`)
  - Path to the gear CSV file containing gear configurations.

- application.csv.ownedGear (default: `./data/owned_gear.csv`)
  - Path to the owned gear CSV file. Used to determine owned gear levels.

- application.defaults.minAgility (default: `90`)
  - Initial minimum agility filter used on the home page.

- application.defaults.minEndurance (default: `50`)
  - Initial minimum endurance filter used on the home page.

- application.defaults.minService (default: `40`)
  - Initial minimum service filter used on the home page.

- application.defaults.minForehand (default: `80`)
  - Initial minimum forehand filter used on the home page.

- application.defaults.minBackhand (default: `80`)
  - Initial minimum backhand filter used on the home page.

- application.defaults.minTotal (default: `350`)
  - Initial minimum total attribute score.

- application.defaults.upgradeAllowed (default: `0`)
  - How many upgrades are allowed by default when generating configurations.

- application.defaults.maxLevel (default: `15`)
  - Maximum player/gear level to use by default when generating configurations.

- application.defaults.pageSize (default: `100`)
  - The page size used for pagination of generated configurations on the home page.

Notes
-----
- Spring Boot relaxed binding means you can also set properties using environment variables or alternative naming (e.g. `APPLICATION_CSV_PLAYERS` or `application.csv.owned-players`).
- For tests, there's a test helper `TestCsvPropertiesFactory` to easily create `CsvProperties` objects pointing to temporary files.

Benchmarks
----------
We include a small integration benchmark that compares the optimized generator to a legacy (baseline) implementation.
The benchmark test is excluded from the default build and runs only when the `benchmark` Maven profile is activated.

How to run
- Default build (benchmarks excluded):

```bash
mvn test
```

- Run benchmarks (includes `ConfigGeneratorBenchmarkTest`):

```bash
mvn -Pbenchmark test
```

- Run only the benchmark test (profile enabled):

```bash
mvn -Pbenchmark -Dtest=ConfigGeneratorBenchmarkTest test
```

Toggles and modes
- The generator supports a legacy baseline mode (no pruning/parallelism) used by the benchmark. The test flips the implementation by setting system property `tennis.generator.legacy`.
  - `-Dtennis.generator.legacy=true` forces legacy mode.
  - `-Dtennis.generator.legacy=false` forces the optimized mode.
  The benchmark test handles switching for you; you can also run experiments manually by adding the system property to the `mvn` command.

Interpreting results
- The benchmark prints a short summary to stdout and logs average durations (ms) for the optimized and legacy runs and the number of configurations found, e.g.:

```
Benchmark summary:
  optimized avg (ms): 123.45
  legacy    avg (ms): 987.65
  results count: 4321
```

- Focus on the average execution time and the result count. The test asserts both implementations produce the same result count. If counts diverge, there is a functional problem.
- Run the benchmark multiple times on your target machine and use the average as a guide — JIT warming and GC mean single-run numbers are noisy.

Tips and options
- To reduce noise and get more consistent results, increase heap and pin CPUs using JVM args. You can add JVM options in the `benchmark` profile in `pom.xml` or pass them directly to Maven (example):

```bash
MAVEN_OPTS="-Xmx4g -XX:+UseG1GC" mvn -Pbenchmark test
```

- For rigorous microbenchmarks consider using JMH instead of a simple integration test; JMH handles warmup, forks, iterations and provides statistically sound results.

Why benchmarks are in a profile
- Benchmarks can be long-running and noisy; keeping them out of the default build keeps CI and developer feedback loops fast while letting you run performance experiments locally or in dedicated perf jobs.
