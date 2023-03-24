package com.example.superherov5.repositories;

import com.example.superherov5.dto.City;
import com.example.superherov5.dto.PowerCount;
import com.example.superherov5.dto.SuperHeroForm;
import com.example.superherov5.dto.SuperPower;
import com.example.superherov5.model.Superhero;

import java.util.List;

public interface ISuperheroRepo {
    List<Superhero> getSuperheroes();
    Superhero getSuperhero(String name);
    SuperPower getPowersForOne(String heroName);
    List<String> getCities();
    List<String> getSuperPowers();
    void addSuperhero(SuperHeroForm form);
    void deleteHero(String heroname);
    void updateHero(SuperHeroForm form, String heroName);

}
