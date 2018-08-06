package com.operatornew.superhero.repository;

import com.operatornew.superhero.model.Superhero;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
//@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SuperheroRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SuperheroRepository superheroRepository;

    @Test
    public void whenFindByPseudonym_thenReturnSuperhero() {
        Superhero superhero = new Superhero();
        superhero.setName("Bruce Wayne");
        superhero.setPseudonym("Batman");
        entityManager.persist(superhero);
        entityManager.flush();

        Superhero found = superheroRepository.findByPseudonym(superhero.getPseudonym());

        assertThat(found.getName(), is(superhero.getName()));
    }
}
