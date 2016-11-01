package io.joggr.test.integration;

import io.joggr.aaa.BootstrapCredentialsSetup;
import io.joggr.aaa.Roles;
import io.joggr.aaa.UnsecuredUserRepository;
import io.joggr.aaa.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
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

import static io.restassured.RestAssured.basic;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseIntegrationTest {
    protected final Logger logger = LoggerFactory.getLogger(UserRegistrationIT.class);

    @Autowired
    private UnsecuredUserRepository users;
    @Autowired
    private PasswordEncoder passwordencoder;
    @Value("${local.server.port}")
    private int port;

    protected RequestSpecification withAdminUser;
    protected ResponseSpecification unauthorised;
    protected ResponseSpecification forbidden;
    protected Map<Roles, RequestSpecification> userWithRole;

    @Before
    public void setUp() throws Exception {
        logger.debug("Test API running on port: {}", port);
        RestAssured.port = port;

        userWithRole = new HashMap<>();
        for (Roles role : Roles.values()) {
            logger.info("Created user with role {} : userName is '{}', passowrd '{}'",
                    users.save(new User(
                            role.name(),
                            passwordencoder.encode(role.name()),
                            Collections.singleton(role))
                    )
            );
            userWithRole.put(
                    role,
                    (new RequestSpecBuilder()).setAuth(basic(role.name(), role.name())).build()
            );
        }

        withAdminUser = (new RequestSpecBuilder())
                .setAuth(basic(BootstrapCredentialsSetup.DEFAULT_ADMIN_NAME, BootstrapCredentialsSetup.DEFAULT_ADMIN_PASSWORD))
                .build();

        unauthorised = (new ResponseSpecBuilder())
                .expectBody("error", equalTo("Unauthorized"))
                .expectStatusCode(401)
                .build();

        forbidden = (new ResponseSpecBuilder())
                .expectBody("error", equalTo("Forbidden"))
                .expectStatusCode(403)
                .build();
    }
}
