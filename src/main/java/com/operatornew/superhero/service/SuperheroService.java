package com.operatornew.superhero.service;

import com.operatornew.superhero.exception.SuperheroNotFound;
import com.operatornew.superhero.model.Superhero;
import org.springframework.stereotype.Service;

@Service
public interface SuperheroService {

    Superhero addAllyToSuperhero(String superHeroPseudonym, String allyPseudonym) throws SuperheroNotFound;

    Superhero findByPseudonym(String pseudonym);

    Iterable<Superhero> findAll();

    Superhero save(Superhero superhero);
}
