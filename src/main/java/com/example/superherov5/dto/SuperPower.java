package com.example.superherov5.dto;

import java.util.List;

public class SuperPower {
    private String heroName;
    private List<String> powers;

    public SuperPower(String heroName, List<String> powers) {
        this.heroName = heroName;
        this.powers = powers;
    }

    public SuperPower() {

    }

    public String getHeroName() {
        return heroName;
    }

    public List<String> getPowers() {
        return powers;
    }

    public void addSuperPower(String name) {
        powers.add(name);
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }


    public void setPowers(List<String> powers) {
        this.powers = powers;
    }
}
