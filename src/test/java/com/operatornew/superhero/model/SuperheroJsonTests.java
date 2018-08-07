package com.operatornew.superhero.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@JsonTest
public class SuperheroJsonTests {

    @Autowired
    private JacksonTester<Superhero> jsonTester;

    @Test
    public void testSerialize() throws Exception {
        Superhero superhero = new Superhero("name", "pseudonym");
        superhero.setFirstPublished(LocalDate.of(2018,8,7));
        superhero.setPublisher("publisher");
        superhero.addSkill(new Skill("skill1"));

        String json = jsonTester.write(superhero).getJson();
        assertThat(json, hasJsonPath("$.name", equalTo("name")));
        assertThat(json, hasJsonPath("$.pseudonym", equalTo("pseudonym")));
        assertThat(json, hasJsonPath("$.publisher", equalTo("publisher")));
        assertThat(json, hasJsonPath("$.firstPublished", equalTo("2018-08-07")));
        assertThat(json, hasJsonPath("$.skills[0].skill", equalTo("skill1")));
    }
}
