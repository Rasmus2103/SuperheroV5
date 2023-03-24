package com.example.superherov5.model;


public class Superhero {
    private int id;
    private String heroName;
    private String realName;
    private int creationYear;

    public Superhero(String heroName, String realName, int creationYear) {
        this.id = id;
        this.heroName = heroName;
        this.realName = realName;
        this.creationYear = creationYear;
    }

    //Get metoder
    public String getHeroName() {
        return heroName;
    }

    public String getRealName() {
        return realName;
    }

    public int getCreationYear() {
        return creationYear;
    }

    //Set metoder
    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setCreationYear(int creationYear) {
        this.creationYear = creationYear;
    }
}
