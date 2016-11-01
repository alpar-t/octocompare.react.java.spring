package io.joggr.test.integration;


import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import io.joggr.aaa.*;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class MajorRolesIT {

    public static final String CONTENT_MANAGER_USER = "test-user-content-manager";
    public static final String CONTENT_MANAGER_PASSWORD = "content-manager-password";
    public static final String TEST_USERNAME = "integration-test-user";
    public static final String TEST_PASSWORD = "integration-test-password";
    private final Logger logger = LoggerFactory.getLogger(MajorRolesIT.class);

    public static final String ADMIN_USER = BootstrapCredentialsSetup.DEFAULT_ADMIN_NAME;
    public static final String ADMIN_PASSWORD = BootstrapCredentialsSetup.DEFAULT_ADMIN_PASSWORD;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UnsecuredUserRepository users;
    @Autowired
    private PasswordEncoder passwordencoder;

    @Before
    public void setUp() throws Exception {
        logger.info("Created content manager: {}",
        users.save(new User(
                CONTENT_MANAGER_USER,
                passwordencoder.encode(CONTENT_MANAGER_PASSWORD),
                Collections.singleton(Roles.ROLE_CONTENT_MANAGER))
        ));
        logger.info("Created user: {}",
                users.save(new User(
                        TEST_USERNAME,
                        passwordencoder.encode(TEST_PASSWORD),
                        Collections.singleton(Roles.ROLE_USER))
                ));
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
        assertThat(red).contains(Roles.ROLE_USER.name());

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

    @Test
    public void contentManagersCantAccessUsers() {
        String response = restTemplate
                .withBasicAuth(CONTENT_MANAGER_USER, CONTENT_MANAGER_PASSWORD)
                .getForObject("/users/" + ADMIN_USER, String.class);
        logger.info("Response for listing users with content manager role: {}", response);
        assertThat(JsonPath.<String>read(response, "$.error")).isEqualTo("Forbidden");
    }

    @Test
    public void regularUserCantAccessUsers() {
        String response = restTemplate
                .withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
                .getForObject("/users/" + ADMIN_USER, String.class);
        logger.info("Response for listing users with content manager role: {}", response);
        assertThat(JsonPath.<String>read(response, "$.error")).isEqualTo("Forbidden");
    }

    @Test
    public void contentManagersCanAccessContent() {
        doGET(
                "/jogEntries",
                restTemplate.withBasicAuth(CONTENT_MANAGER_USER, CONTENT_MANAGER_PASSWORD)
        );
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

}
