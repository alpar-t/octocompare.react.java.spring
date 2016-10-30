package io.joggr.test.integration;


import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataAPIGeneratedIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void halLinksPresent() {
        String body = restTemplate.getForObject("/", String.class);

        Map links = JsonPath.read(body, "$._links");
        assertThat(links).isNotEmpty();
    }




}
