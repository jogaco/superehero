package com.operatornew.superhero.controller;

import com.operatornew.superhero.model.Superhero;
import com.operatornew.superhero.repository.SuperheroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/superheroes")
public class SuperheroController {

    @Autowired
    SuperheroRepository superheroRepository;

    public SuperheroController(SuperheroRepository superheroRepository) {
        this.superheroRepository = superheroRepository;
    }

    @RequestMapping(value = "/{pseudonym}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Superhero> get(@PathVariable("pseudonym") String pseudonym) {
        HttpHeaders headers = new HttpHeaders();
        headers.add (HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Superhero superhero = superheroRepository.findByPseudonym(pseudonym);
        if (superhero != null) {
            return new ResponseEntity<>(superhero, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Iterable<Superhero>> getAll() {
        HttpHeaders headers = new HttpHeaders();
        Iterable<Superhero> all = superheroRepository.findAll();

        return new ResponseEntity<>(all, headers, HttpStatus.OK);
    }

}
