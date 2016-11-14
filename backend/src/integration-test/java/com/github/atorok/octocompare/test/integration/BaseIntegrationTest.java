package com.github.atorok.octocompare.test.integration;

import com.github.atorok.octocompare.aaa.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseIntegrationTest {
    private final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @Autowired
    private UserRepository users;
    @Autowired
    protected PasswordEncoder passwordencoder;
    @Value("${local.server.port}")
    private int port;

    protected RequestSpecification withAdminUser;
    protected ResponseSpecification unauthorised;
    protected ResponseSpecification forbidden;
    protected Map<Roles, RequestSpecification> userWithRole;

    @Test
    public void checkBasicAccess() {
        when()
                .get("/users")
        .then()
                .log().all()
                .spec(unauthorised);

        given()
                .spec(withAdminUser)
        .when()
                .get("/")
        .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void checkTestUsersCreated() {
        given()
                .spec(withAdminUser)
        .when()
                .get("/users")
        .then()
                .log().all()
                .statusCode(200)
                .body("_embedded.users",
                        hasSize(greaterThan(Roles.values().length))
                );
        ;
    }

    @Before
    public void setUp() throws Exception {
        logger.debug("Test API running on port: {}", port);
        RestAssured.port = port;

        userWithRole = new HashMap<>();
        for (Roles role : Roles.values()) {
            try (AsInternalUser __ = new AsInternalUser()) {
                logger.info("Created user with role {} : userName is '{}', passowrd '{}'",
                        users.save(new User(
                                role.name(),
                                passwordencoder.encode(role.name()),
                                Collections.singleton(role))
                        ), role, role
                );
            }
            userWithRole.put(
                    role,
                    (new RequestSpecBuilder()).setAuth(basic(role.name(), role.name())).build()
            );
        }

        withAdminUser = (new RequestSpecBuilder())
                .setAuth(basic(BootstrapCredentialsSetup.DEFAULT_ADMIN_NAME, BootstrapCredentialsSetup.DEFAULT_ADMIN_PASSWORD))
                .build();

        unauthorised = (new ResponseSpecBuilder())
                .expectStatusCode(401)
                .expectBody("error", equalTo("Unauthorized"))
                .build();

        forbidden = (new ResponseSpecBuilder())
                .expectStatusCode(403)
                .expectBody("error", equalTo("Forbidden"))
                .build();
    }
}
