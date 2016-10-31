package io.joggr.test.integration;


import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import io.joggr.Roles;
import io.joggr.aaa.User;
import io.joggr.aaa.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class ApiSecurityIT {

    private final Logger logger = LoggerFactory.getLogger(ApiSecurityIT.class);

    public static final String ADMIN_USER = "integration-test-user-admin";
    public static final String ADMIN_PASSWORD = "integration-test-password";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository users;
    @Autowired
    private PasswordEncoder passwordencoder;


    @Before
    public void setUp() {
        logger.info("Creating admin user: {}:{}", ADMIN_USER, ADMIN_PASSWORD);
        users.save(new User(ADMIN_USER, passwordencoder.encode(ADMIN_PASSWORD), Arrays.asList(Roles.values())));
    }

    @Test
    public void halLinksPresent() {
        doGETasAdmin("/");
    }

    @Test
    public void testRegisterAndListThenDelete() {
        doPOSTasNobody(
                "/users/signUp/testu",
                "testp"
        );

        logger.info("Now try that again");
        try {
            doPOSTasNobody(
                    "/users/signUp/testu",
                    "testpop"
            );
            fail("No user already exists error");
        } catch (AssertionError e) {
            logger.debug("Raised user already exists as it should", e);
        }

        logger.info("Trying to log in as the new user");
        doGET(
                "/",
                this.restTemplate.withBasicAuth("testu", "testp")
        );

        logger.info("Listing users as admin");
        String createdUser = doGETasAdmin("/users/testu");
        List red = JsonPath.<List>read(createdUser, "$.authorities");
        assertThat(red.size()).isEqualTo(1);
        assertThat(red).contains(Roles.USER_ROLE.name());

        restTemplate
                .withBasicAuth(ADMIN_USER, ADMIN_PASSWORD)
                .delete("/users/testu");

        try {
            doGET(
                    "/",
                    this.restTemplate.withBasicAuth("testu", "testp")
            );
            fail("Managed to log in after user was deleted!");
        } catch (AssertionError e){
            logger.debug("Authentication failed after delete, as it should", e);
        }
    }

    private <T> String doPOSTasNobody(String url, T body) {
        return doPOST(url, body, this.restTemplate);
    }

    private <T> String doPOST(String url, T body, TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String response = restTemplate
                .postForObject(
                        url,
                        new HttpEntity<T>(
                                body,
                                headers
                        ),
                        String.class
                );
        logger.info("POST {}: {}", url, response);
        try {
            assertThat(JsonPath.<String>read(response, "$.error")).isEmpty();
        } catch (PathNotFoundException e) {
            logger.debug("Post returned without error (thus the exception)", e);
        }
        return response;

    }

    private String doGETasAdmin(String url) {
        return doGET(url, restTemplate.withBasicAuth(ADMIN_USER, ADMIN_PASSWORD));
    }

    private String doGET(String url, TestRestTemplate testRestTemplate) {
        String response = testRestTemplate.getForObject(url, String.class);
        logger.info("GET {} : {}", url, response);
        try {
            assertThat(JsonPath.<String>read(response, "$.error")).isEmpty();
        } catch (PathNotFoundException e) {
            logger.debug("GET returned without error (thus the exception)", e);
        }
        assertThat(JsonPath.<Map>read(response, "$._links")).isNotEmpty();
        return response;
    }


    @Test
    public void createAndDelete() {

    }

}
