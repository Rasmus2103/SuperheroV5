package com.example.superherov5.controller;

import com.example.superherov5.dto.SuperHeroForm;
import com.example.superherov5.dto.SuperPower;
import com.example.superherov5.model.Superhero;
import com.example.superherov5.repositories.ISuperheroRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path="/")
public class SuperheroController {
    private ISuperheroRepo superheroRepo;

    public SuperheroController(ApplicationContext context, @Value("${superhero.repository.impl}") String impl) {
        superheroRepo = (ISuperheroRepo) context.getBean(impl);
    }

    @GetMapping("superheroes")
    public String getSuperheroes(Model model) {
        List<Superhero> superheroList = superheroRepo.getSuperheroes();
        model.addAttribute("superheroList", superheroList);
        return "superheroList";
    }

    @GetMapping("superheroes/powers/{heroId}")
    public String getPowers(@PathVariable("heroId") int heroId, Model model) {
        SuperPower superPower = superheroRepo.getPowersForOne(heroId);
        model.addAttribute("name", superPower.getHeroName());
        model.addAttribute("powers", superPower.getPowers());
        return "powers";
    }

    @GetMapping("/register")
    public String showForm(Model model) {
        SuperHeroForm superHeroForm = new SuperHeroForm();
        model.addAttribute("superhero", superHeroForm);

        List<String> listCities = superheroRepo.getCities();
        model.addAttribute("listCities", listCities);

        List<String> listSuperPowers = superheroRepo.getSuperPowers();
        model.addAttribute("listSuperPowers", listSuperPowers);

        return "createHero";
    }

    @PostMapping("/register")
    public String addSuperhero(@ModelAttribute("superhero") SuperHeroForm form, Model model) {
        superheroRepo.addSuperhero(form);
        List<String> listCities = superheroRepo.getCities();
        model.addAttribute("listCities", listCities);

        List<String> listSuperPowers = superheroRepo.getSuperPowers();
        model.addAttribute("listSuperPowers", listSuperPowers);
        System.out.println(form);
        return "createdResult";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        SuperHeroForm superhero = superheroRepo.findSuperHeroById(id);
        List<String> allPowers = superheroRepo.getSuperPowers();
        model.addAttribute("superhero", superhero);
        model.addAttribute("listCities", superheroRepo.getCities());
        model.addAttribute("listSuperPowers", allPowers);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String updateSuperhero(@PathVariable("id") int id, @ModelAttribute("superhero") SuperHeroForm superhero, Model model) {
        superheroRepo.updateHero(id, superhero);
        model.addAttribute("successMessage", "Superhero successfully updated!");
        return "createdResult";
    }



    @GetMapping("superheroes/slet/{heroId}")
    public String deleteHero(@PathVariable("heroId") int heroId, Model model) {
        superheroRepo.deleteHero(heroId);
        model.addAttribute("superheroList", superheroRepo.getSuperheroes());
        return "redirect:/superheroes";
    }

}

