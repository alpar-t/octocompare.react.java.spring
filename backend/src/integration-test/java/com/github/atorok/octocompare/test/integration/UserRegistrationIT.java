package com.github.atorok.octocompare.test.integration;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;


public class UserRegistrationIT extends BaseIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(UserRegistrationIT.class);

    @Test
    public void checkUserRegistration() {
        logger.info("Registering a new user");
        final String testUser = "testu";
        final String testPass = "testp";

        given()
                .pathParam("userName", testUser)
                .body(testPass)
        .when()
                .post("users/sign-up/{userName}")
        .then()
                .log().all()
                .statusCode(200);

        logger.info("Then try registering the same user again to prove it doesn't work");
        given()
                .pathParam("userName", testUser)
                .body("testp-other")
        .when()
                .post("users/sign-up/{userName}")
        .then()
                .log().all()
                .statusCode(409);

        logger.info("Show that the new user can authenticate and access itself");
        given()
                .auth().basic(testUser, testPass)
                .pathParam("userName", testUser)
        .when()
                .get("/users/{userName}")
        .then()
                .log().all()
                .statusCode(200);

        logger.info("Now have the user delete itself");
        given()
                .pathParam("userName", testUser)
                .auth().basic(testUser, testPass)
        .when()
                .delete("/users/{userName}")
        .then()
                .log().all()
                .statusCode(204);

        logger.info("And prove that the user can no longer authenticate");
        given()
                .auth().basic(testUser, testPass)
                .pathParam("userName", testUser)
        .when()
                .get("/users/{userName}")
        .then()
                .log().all()
                .spec(unauthorised);
    }


}
