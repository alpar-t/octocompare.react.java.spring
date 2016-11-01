package io.joggr.test.integration;


import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;


public class UserRegistrationIT extends BaseIntegrationTest {

    @Test
    public void checkBasicAccess() {
        when()
                .get("/")
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
    public void checkUserRegistration() {
        logger.info("Registering a new user");
        final String testUser = "testu";
        final String testPass = "testp";

        given()
                .pathParam("userName", testUser)
                .body(testPass)
        .when()
                .post("users/signUp/{userName}")
        .then()
                .log().all()
                .statusCode(200);

        logger.info("Then try registering the same user again to prove it doesn't work");
        given()
                .pathParam("userName", testUser)
                .body("testp-other")
        .when()
                .post("users/signUp/{userName}")
        .then()
                .log().all()
                .statusCode(409);

        logger.info("Show that the new user works");
        given()
                .auth().basic(testUser, testPass)
        .when()
                .get("/")
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
        .when()
                .get("/")
        .then()
                .log().all()
                .spec(unauthorised);
    }


}
