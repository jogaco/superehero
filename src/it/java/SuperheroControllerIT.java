import com.operatornew.superhero.SuperheroApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SuperheroApplication.class)
//@ActiveProfiles("test")
@Sql({ "classpath:remove-data.sql", "classpath:data.sql.test" })
public class SuperheroControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private HttpHeaders headers = new HttpHeaders();

    @Test
    public void notFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/superheroes/blah-blah"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void found() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/superheroes/spiderman"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        String body = response.getBody();
        assertThat(body, hasJsonPath("$.name"));
        assertThat(body, hasJsonPath("$.pseudonym"));
        assertThat(body, hasJsonPath("$.skills[0].skill"));
    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }
}