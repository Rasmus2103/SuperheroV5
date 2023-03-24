package com.example.superherov5.dto;

public class PowerCount {
    private String heroName;
    private String realName;
    private int countPower;

    public PowerCount(String heroName, String realName, int countPower) {
        this.heroName = heroName;
        this.realName = realName;
        this.countPower = countPower;
    }

    public PowerCount() {

    }

    public String getHeroName() {
        return heroName;
    }

    public String getRealName() {
        return realName;
    }

    public int getCountPower() {
        return countPower;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public void setCountPower(int countPower) {
        this.countPower = countPower;
    }
}
