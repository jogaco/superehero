package com.operatornew.superhero.service.impl;

import com.operatornew.superhero.exception.SuperheroNotFound;
import com.operatornew.superhero.model.Superhero;
import com.operatornew.superhero.repository.SuperheroRepository;
import com.operatornew.superhero.service.SuperheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SuperheroServiceImpl implements SuperheroService {
    @Autowired
    SuperheroRepository superheroRepository;

    @Override
    @Transactional
    public Superhero addAllyToSuperhero(String superHeroPseudonym, String allyPseudonym) throws SuperheroNotFound {
        Superhero superhero = superheroRepository.findByPseudonym(superHeroPseudonym);
        if (superhero == null) {
            throw new SuperheroNotFound("Superhero not found: " + superHeroPseudonym);
        }
        Superhero theAlly = superheroRepository.findByPseudonym(allyPseudonym);
        if (theAlly == null) {
            throw new SuperheroNotFound("Ally not found: " + allyPseudonym);
        }
        superhero.addAlly(theAlly);

        if (superhero != theAlly && !theAlly.isAlly(superhero)) {
            theAlly.addAlly(superhero);
            superheroRepository.save(theAlly);
        }

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
