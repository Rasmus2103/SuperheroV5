package com.example.superherov5.dto;

import java.util.List;

public class City {
    private String cityName;
    private List<String> heroName;

    public City(String cityName, List<String> heroName) {
        this.heroName = heroName;
        this.cityName = cityName;
    }

    public City() {

    }

    public List<String> getHeroName() {
        return heroName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setHeroName(List<String> heroName) {
        this.heroName = heroName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void addHero(String name) {
        heroName.add(name);
    }
}
