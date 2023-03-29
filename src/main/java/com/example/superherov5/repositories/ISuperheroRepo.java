package com.example.superherov5.repositories;

import com.example.superherov5.dto.SuperHeroForm;
import com.example.superherov5.dto.SuperPower;
import com.example.superherov5.model.Superhero;

import java.util.List;

public interface ISuperheroRepo {
    List<Superhero> getSuperheroes();
    //Superhero getSuperhero(String name);
    SuperHeroForm findSuperHeroById(int id);
    SuperPower getPowersForOne(int heroId);
    List<String> getCities();
    List<String> getSuperPowers();
    List<String> getSuperheroPowers(int id);
    void addSuperhero(SuperHeroForm form);
    void deleteHero(int heroId);
    void updateHero(int id, SuperHeroForm form);
    void deleteSuperheroPowers(int heroId);
    void addSuperheroPowers(int heroId, List<String> powers);
    int getPowerId(String powerName);

}
