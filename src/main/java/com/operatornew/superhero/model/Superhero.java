package com.operatornew.superhero.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"allies"}, allowGetters = true)
public class Superhero {

    @Id
    @NotBlank
    private String pseudonym;

    @Column
    @NotBlank
    private String name;

    @Column
    private String publisher;

    @Column
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate firstPublished;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "superhero_skills",
            joinColumns = { @JoinColumn(name = "superhero_id") },
            inverseJoinColumns = { @JoinColumn(name = "skill_id") }
    )
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "superhero_allies",
            joinColumns = { @JoinColumn(name = "superhero_id") },
            inverseJoinColumns = { @JoinColumn(name = "ally_id") }
    )
    @JsonBackReference
    private Set<Superhero> allies = new HashSet<>();

    public Superhero() {}

    public Superhero(String name, String pseudonym) {
        this.name = name;
        this.pseudonym = pseudonym;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getFirstPublished() {
        return firstPublished;
    }

    public void setFirstPublished(LocalDate firstPublished) {
        this.firstPublished = firstPublished;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public Set<Superhero> getAllies() {
        return allies;
    }

    public void setAllies(Set<Superhero> allies) {
        this.allies = allies;
    }

    public void addAlly(Superhero ally) {
        allies.add(ally);
    }

    public boolean isAlly(Superhero superhero) {
        return allies.contains(superhero);
    }

    @PrePersist
    @PreUpdate
    private void prepare() {
        if (pseudonym != null) {
            pseudonym = pseudonym.toLowerCase();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Superhero)) return false;
        Superhero superhero = (Superhero) o;
        return Objects.equals(pseudonym, superhero.pseudonym);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (pseudonym != null)
            hash += pseudonym.hashCode();
        return hash;
    }
}
