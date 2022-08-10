package com.sweber.tennis.model.config;

public class Attributes implements Comparable {
    protected int agility;
    protected int endurance;
    protected int service;
    protected int volley;
    protected int forehand;
    protected int backhand;

    public Attributes() {
    }

    public Attributes(int agility, int endurance, int service, int volley, int forehand, int backhand) {
        this.agility = agility;
        this.endurance = endurance;
        this.service = service;
        this.volley = volley;
        this.forehand = forehand;
        this.backhand = backhand;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getVolley() {
        return volley;
    }

    public void setVolley(int volley) {
        this.volley = volley;
    }

    public int getForehand() {
        return forehand;
    }

    public void setForehand(int forehand) {
        this.forehand = forehand;
    }

    public int getBackhand() {
        return backhand;
    }

    public void setBackhand(int backhand) {
        this.backhand = backhand;
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "agility=" + agility +
                ", endurance=" + endurance +
                ", service=" + service +
                ", volley=" + volley +
                ", forehand=" + forehand +
                ", backhand=" + backhand +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return 1;
        Attributes target = (Attributes) o;
        return agility >= target.getAgility()
                && endurance >= target.getEndurance()
                && service >= target.getService()
                && volley >= target.getVolley()
                && forehand >= target.getForehand()
                && backhand >= target.getBackhand() ? 1 : 0;
    }
}
