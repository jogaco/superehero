package com.operatornew.superhero.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Superhero {

    @Id
    private String pseudonym;

    @Column
    private String name;

    @Column
    private LocalDate firstPublished;

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

    public LocalDate getFirstPublished() {
        return firstPublished;
    }

    public void setFirstPublished(LocalDate firstPublished) {
        this.firstPublished = firstPublished;
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
