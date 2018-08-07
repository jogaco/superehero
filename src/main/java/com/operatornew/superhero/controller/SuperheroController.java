package com.operatornew.superhero.controller;

import com.operatornew.superhero.model.Ally;
import com.operatornew.superhero.model.Superhero;
import com.operatornew.superhero.service.SuperheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping(path="/superheroes")
public class SuperheroController {

    @Autowired
    SuperheroService superheroService;

    public SuperheroController(SuperheroService superheroService) {
        this.superheroService = superheroService;
    }

    @RequestMapping(value = "/{pseudonym}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Superhero> get(@PathVariable("pseudonym") String pseudonym) {
        HttpHeaders headers = new HttpHeaders();
        headers.add (HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Superhero superhero = superheroService.findByPseudonym(pseudonym);
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
        Iterable<Superhero> all = superheroService.findAll();

        return new ResponseEntity<>(all, headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Superhero> post(@Valid @RequestBody Superhero superhero) throws EntityExistsException {
        Superhero saved = superheroService.save(superhero);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pseudonym}")
                .buildAndExpand(saved.getPseudonym())
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, location.toString());
        return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{pseudonym}/allies", method = RequestMethod.POST)
    public ResponseEntity<Superhero> postAlly(@PathVariable("pseudonym") String pseudonym, @RequestBody Ally ally) {

        superheroService.addAllyToSuperhero(ally, pseudonym);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

}
