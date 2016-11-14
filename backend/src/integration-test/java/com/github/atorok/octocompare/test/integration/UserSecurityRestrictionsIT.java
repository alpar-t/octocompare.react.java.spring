package com.github.atorok.octocompare.test.integration;


import com.google.common.collect.ImmutableMap;
import com.github.atorok.octocompare.aaa.Roles;
import io.restassured.http.ContentType;
import org.junit.Test;

import static com.github.atorok.octocompare.aaa.Roles.ROLE_CONTENT_MANAGER;
import static com.github.atorok.octocompare.aaa.Roles.ROLE_USER;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class UserSecurityRestrictionsIT extends BaseIntegrationTest {

    @Test
    public void usersCanNotAccessOtherUsers() {
        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
                .get("/users")
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }

    @Test
    public void contentManagersCanNotAccessOtherUsers() {
        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
        .when()
                .get("/users")
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }

    @Test
    public void noUnauthenticatedAccessToUsers() {
        when()
                .get("/users")
        .then()
                .log().all()
                .spec(unauthorised)
        ;
    }

    @Test
    public void usersCanNotUpdateThemselves() {
        // Note this would require specific password change API to prevent users from escalating their roles
        given()
                .spec(userWithRole.get(ROLE_USER))
                .pathParam("userName", ROLE_USER.name())
                .body(ImmutableMap.of(
                        "password", "someOtherPassword"
                ))
                .contentType(ContentType.JSON)
        .when()
                .patch("/users/{userName}")
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }

    @Test
    public void contentManagersCanNotUpdateThemselves() {
        // Note this would require specific password change API to prevent users from escalating their roles
        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
                .pathParam("userName", ROLE_CONTENT_MANAGER.name())
                .body(ImmutableMap.of(
                        "password", "someOtherPassword"
                ))
                .contentType(ContentType.JSON)
        .when()
                .patch("/users/{userName}")
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }


    @Test
    public void usersCanNotCreateNewUsers() {
        given()
                .spec(userWithRole.get(ROLE_USER))
                .body(ImmutableMap.of(
                        "userName", "testu",
                        "password", "password",
                        "authorities",  Roles.values()
                        ))
                .contentType(ContentType.JSON)
        .when()
                .post("/users")
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }

    @Test
    public void contentManagersCanNotCreateNewUsers() {
        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
                .body(ImmutableMap.of(
                        "userName", "testu",
                        "password", "password",
                        "authorities",  Roles.values()
                ))
                .contentType(ContentType.JSON)
        .when()
                .post("/users")
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }


}
