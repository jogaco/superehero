package com.operatornew.superhero.service.impl;

import com.operatornew.superhero.model.Ally;
import com.operatornew.superhero.model.Superhero;
import com.operatornew.superhero.repository.SuperheroRepository;
import com.operatornew.superhero.service.SuperheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuperheroServiceImpl implements SuperheroService {
    @Autowired
    SuperheroRepository superheroRepository;

    @Override
    public Superhero addAllyToSuperhero(Ally ally, String superHeroPseudonym) {
        Superhero superhero = superheroRepository.findByPseudonym(superHeroPseudonym);
        Superhero theAlly = superheroRepository.findByPseudonym(ally.getPseudonym());
        superhero.addAlly(theAlly);
        return superheroRepository.save(superhero);
    }

    @Override
    public Superhero findByPseudonym(String pseudonym) {
        return superheroRepository.findByPseudonym(pseudonym);
    }

    @Override
    public Iterable<Superhero> findAll() {
        return superheroRepository.findAll();
    }

    @Override
    public Superhero save(Superhero superhero) {
        return superheroRepository.save(superhero);
    }
}
