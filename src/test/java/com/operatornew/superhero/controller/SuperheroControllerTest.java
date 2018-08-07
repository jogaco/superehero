package com.operatornew.superhero.controller;

import com.operatornew.superhero.model.Skill;
import com.operatornew.superhero.model.Superhero;
import com.operatornew.superhero.repository.SuperheroRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SuperheroController.class)
public class SuperheroControllerTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                  MediaType.APPLICATION_JSON.getSubtype(),
                                                  Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    private HttpMessageConverter jsonMessageConverter;

    @MockBean
    private SuperheroRepository superheroRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.jsonMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.jsonMessageConverter);
    }

    @Before
    public void setup() {
        Mockito.reset(superheroRepository);
    }

    @Test
    public void found() throws Exception {
        Superhero superhero = new Superhero("name", "pseudonym");
        when(superheroRepository.findByPseudonym("pseudonym")).thenReturn(superhero);

        mockMvc.perform(get("/superheroes/pseudonym")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(superhero.getName())))
                .andExpect(jsonPath("$.pseudonym", is(superhero.getPseudonym())));
    }
    @Test
    public void notFound() throws Exception {
        mockMvc.perform(get("/superheroes/blah-blah")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void all() throws Exception {
        Superhero superhero = new Superhero("name", "pseudonym");
        when(superheroRepository.findAll()).thenReturn(Collections.singletonList(superhero));

        mockMvc.perform(get("/superheroes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(superhero.getName())))
                .andExpect(jsonPath("$[0].pseudonym", is(superhero.getPseudonym())));
    }

    @Test
    public void post() throws Exception {
        Superhero superhero = new Superhero("name", "pseudonym");
        Skill skill = new Skill("skill1");
        superhero.addSkill(skill);
        when(superheroRepository.save(superhero)).thenReturn(superhero);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/superheroes")
                .content(json(superhero))
                .contentType(contentType);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(superhero.getName())))
                .andExpect(jsonPath("$.pseudonym", is(superhero.getPseudonym())))
                .andExpect(jsonPath("$.skills[0].skill", is(skill.getSkill())));
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        jsonMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
