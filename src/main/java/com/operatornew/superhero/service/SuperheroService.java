package com.operatornew.superhero.service;

import com.operatornew.superhero.model.Ally;
import com.operatornew.superhero.model.Superhero;
import org.springframework.stereotype.Service;

@Service
public interface SuperheroService {

    Superhero addAllyToSuperhero(Ally ally, String superHeroPseudonym);

    Superhero findByPseudonym(String pseudonym);

    Iterable<Superhero> findAll();

    Superhero save(Superhero superhero);
}
