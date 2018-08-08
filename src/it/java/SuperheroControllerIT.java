import com.operatornew.superhero.SuperheroApplication;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SuperheroApplication.class)
@Sql({ "classpath:remove-data.sql", "classpath:data.sql.test" })
public class SuperheroControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void notFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/superheroes/blah-blah"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void found() {
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/superheroes/spiderman"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        String body = response.getBody();
        assertThat(body, hasJsonPath("$.name"));
        assertThat(body, hasJsonPath("$.pseudonym"));
        assertThat(body, hasJsonPath("$.skills[0].skill"));
        assertThat(body, hasJsonPath("$.allies[0].pseudonym"));
    }


    @Test
    public void post() {
        String requestJson = "{\"name\":\"name1\",\"pseudonym\":\"pseudonym-test\",\"skills\":[{\"skill\":\"skill-test\"}]}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/superheroes"),HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        assertThat(location, Matchers.matchesPattern(".*/superheroes/pseudonym-test"));
        String body = response.getBody();
        assertThat(body, hasJsonPath("$.name"));
        assertThat(body, hasJsonPath("$.pseudonym"));
        assertThat(body, hasJsonPath("$.skills"));
        assertThat(body, hasJsonPath("$.skills[0].skill"));
    }

    @Test
    public void postAlly() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/superheroes/spiderman/allies/spiderman"),HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }

    @Test
    public void postAllyNotFound() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/superheroes/spiderman/allies/unexistent"),HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }
}