package io.joggr.test.integration;

import com.google.common.collect.ImmutableMap;
import io.joggr.aaa.Roles;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static io.joggr.aaa.Roles.ROLE_USER;
import static io.joggr.aaa.Roles.ROLE_USER_MANAGER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class UserSecurityPermissionsIT extends BaseIntegrationTest{

    private final Logger logger = LoggerFactory.getLogger(UserSecurityPermissionsIT.class);

    @Test
    public void  userManagerCanAccessAllUsers() {
        logger.info("{} can list all users", ROLE_USER_MANAGER);
        given()
                .spec(userWithRole.get(ROLE_USER_MANAGER))
        .when()
                .get("/users")
        .then()
                .log().all()
                .statusCode(200)
                .body("_embedded.users",
                        hasSize(Roles.values().length + 1 /*the admin account*/)
                )
        ;

        logger.info("{} can access other users", ROLE_USER_MANAGER);
        given()
                .spec(userWithRole.get(ROLE_USER_MANAGER))
                .pathParam("userName", ROLE_USER.name())
        .when()
                .get("/users/{userName}")
        .then()
                .log().all()
                .statusCode(200)
                .body("authorities", equalTo(Arrays.asList(ROLE_USER.name())))
        ;
    }

    @Test
    public void userManagerCanDeleteOtherUsers() {
        logger.info("Using {} to delete user for {}", ROLE_USER_MANAGER, ROLE_USER);
        given()
                .spec(userWithRole.get(ROLE_USER_MANAGER))
                .pathParam("userName", ROLE_USER.name())
        .when()
                .delete("/users/{userName}")
        .then()
                .log().all()
                .statusCode(204)
        ;

        logger.info("Then list all users to make sure no longer there");
        given()
                .spec(userWithRole.get(ROLE_USER_MANAGER))
                .pathParam("userName", ROLE_USER.name())
        .when()
                .get("/users/{userName}")
        .then()
                .log().all()
                .statusCode(404)
        ;
    }

    @Test
    public void userManagerCanUpdatePasswordOfOther() {
        final String someOtherPassword = "someOtherPassword";
        given()
                .spec(userWithRole.get(ROLE_USER_MANAGER))
                .pathParam("userName", ROLE_USER.name())
                .body(ImmutableMap.of(
                        "password", someOtherPassword
                ))
                .contentType(ContentType.JSON)
        .when()
                .patch("/users/{userName}")
        .then()
                .log().all()
                .statusCode(200)
        ;

        logger.info("Show that the user can no longer authenticate with old password");
        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
                .get("/")
        .then()
                .spec(unauthorised)
        ;
    }

}
