package com.operatornew.superhero.repository;

import com.operatornew.superhero.model.Superhero;
import org.springframework.data.repository.CrudRepository;

public interface SuperheroRepository extends CrudRepository<Superhero, Long> {

    Superhero findByPseudonym(String psedonym);

}