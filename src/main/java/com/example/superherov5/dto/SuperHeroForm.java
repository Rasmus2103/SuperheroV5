package com.example.superherov5.dto;

import java.util.List;

public class SuperHeroForm {
    private int heroId;
    private String heroName;
    private String realName;
    private int creationYear;
    private String city;
    List<String> powerList;

    public SuperHeroForm(int heroId, String heroName, String realName, int creationYear, String city, List<String> powerList) {
        this.heroId = heroId;
        this.heroName = heroName;
        this.realName = realName;
        this.creationYear = creationYear;
        this.city = city;
        this.powerList = powerList;
    }

    public SuperHeroForm() { // default konstruktør skal laves

    }

    public void add(String power) {

        powerList.add(power);

    }

    public int getHeroId() {
        return heroId;
    }

    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getCreationYear() {
        return creationYear;
    }

    public void setCreationYear(int creationYear) {
        this.creationYear = creationYear;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getPowerList() {
        return powerList;
    }

    public void setPowerList(List<String> powerList) {
        this.powerList = powerList;
    }

    @Override
    public String toString() {
        return "SuperHeroForm{" +
                "heroId=" + heroId +
                ", heroName='" + heroName + '\'' +
                ", realName='" + realName + '\'' +
                ", creationYear=" + creationYear +
                ", city='" + city + '\'' +
                ", powerList=" + powerList +
                '}';
    }
}
