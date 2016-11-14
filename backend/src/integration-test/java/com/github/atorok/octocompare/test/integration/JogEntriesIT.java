package com.github.atorok.octocompare.test.integration;

import com.github.atorok.octocompare.domain.JogEntry;
import com.google.common.collect.ImmutableMap;
import com.github.atorok.octocompare.aaa.Roles;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.net.URI;

import static com.github.atorok.octocompare.aaa.Roles.ROLE_CONTENT_MANAGER;
import static com.github.atorok.octocompare.aaa.Roles.ROLE_USER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class JogEntriesIT extends BaseIntegrationTest {

    public static final int ANY_DISTANCE = 3000;
    public static final int ANY_DURRATION = 600;

    @Test
    public void userCanPOSTJogEntries() {
        createJogEntries(ROLE_USER);
    }
    private String createJogEntries(Roles asRole) {
        return createJogEntries(asRole, new JogEntry(ANY_DISTANCE, ANY_DURRATION));
    }
    private String createJogEntries(Roles asRole, JogEntry jogEntry) {
        if (asRole.equals(ROLE_CONTENT_MANAGER)) {
            // Content manages are expected to procide explicit userNames
            jogEntry.setUserName(ROLE_CONTENT_MANAGER.name());
        }
        String createdLink = given()
                .spec(userWithRole.get(asRole))
                .contentType(ContentType.JSON)
                .body(jogEntry)
        .when()
                .post("/jogEntries")
        .then()
                .log().all()
                .statusCode(201)
        .extract()
                .path("_links.self.href")
        ;

        String path = URI.create(createdLink).getPath();
        given()
                .spec(userWithRole.get(asRole))
        .when()
                .get(path)
        .then()
                .statusCode(200)
                .body("distanceMeters", equalTo(ANY_DISTANCE))
                .body("timeSeconds", equalTo(ANY_DURRATION))
                .body("userName", equalTo(asRole.name()))
        ;
        return path;
    }

    @Test
    public void userCanPUTJogEntries() {

        JogEntry jogEntry = new JogEntry(ANY_DISTANCE, ANY_DURRATION);

        given()
                .spec(userWithRole.get(ROLE_USER))
                .contentType(ContentType.JSON)
                .pathParam("jogId", jogEntry.getId())
                .body(jogEntry)
        .when()
                .put("/jogEntries/{jogId}")
        .then()
                .log().all()
                .statusCode(201)
        ;

        given()
                .spec(userWithRole.get(ROLE_USER))
                .pathParam("jogId", jogEntry.getId())
        .when()
                .get("/jogEntries/{jogId}")
        .then()
                .statusCode(200)
                .body("distanceMeters", equalTo(ANY_DISTANCE))
                .body("timeSeconds", equalTo(ANY_DURRATION))
                .body("userName", equalTo(ROLE_USER.name()))
        ;

    }

    @Test
    public void usersCanNotCreateForOthers() {
        JogEntry jogEntry = new JogEntry(ANY_DISTANCE, ANY_DURRATION);
        jogEntry.setUserName(ROLE_CONTENT_MANAGER.name());
        // the provided username is ignored and the record is created for the user
        createJogEntries(ROLE_USER, jogEntry);
    }

    @Test
    public void internalFindForbidden() {
        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
            .get("jogEntries/search/findByIdAndUserName")
        .then()
                .log().all()
                .statusCode(404)
        ;
    }

    @Test
    public void userCanNotListAll() {
        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
                .get("/jogEntries")
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }

    @Test
    public void contentManagerCanListAll() {
        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
        .when()
                .get("/jogEntries")
        .then()
                .log().all()
                .statusCode(200)
        ;
    }

    @Test
    public void userCanSearchOnlyHis() {
        given()
                .spec(userWithRole.get(ROLE_USER))
                .queryParam("userName", ROLE_CONTENT_MANAGER.name())
        .when()
                .get("/jogEntries/search/findByUserName")
        .then()
                .log().all()
                .spec(forbidden)
        ;

        given()
                .spec(userWithRole.get(ROLE_USER))
                .queryParam("userName", ROLE_USER.name())
        .when()
                .get("/jogEntries/search/findByUserName")
        .then()
                .log().all()
                .statusCode(200)
        ;
    }

    @Test
    public void contentManagerCanSearchAny() {
        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
                .queryParam("userName", ROLE_CONTENT_MANAGER.name())
        .when()
                .get("/jogEntries/search/findByUserName")
        .then()
                .log().all()
                .statusCode(200)
        ;

        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
                .queryParam("userName", ROLE_USER.name())
        .when()
                .get("/jogEntries/search/findByUserName")
        .then()
                .log().all()
                .statusCode(200)
        ;
    }

    @Test
    public void userCanUpdateJogEntry() {
        String pathToEntry = createJogEntries(ROLE_USER);

        given()
                .spec(userWithRole.get(ROLE_USER))
                .contentType(ContentType.JSON)
                .body(new JogEntry(ANY_DISTANCE + 1, ANY_DURRATION + 1))
        .when()
                .put(pathToEntry)
        .then()
                .log().all()
                .statusCode(200)
        ;

        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
                .get(pathToEntry)
        .then()
                .statusCode(200)
                .body("distanceMeters", equalTo(ANY_DISTANCE + 1))
                .body("timeSeconds", equalTo(ANY_DURRATION + 1))
                .body("userName", equalTo(ROLE_USER.name()))
        ;
    }

    @Test
    public void userCanPatch() {
        String pathToEntry = createJogEntries(ROLE_USER);

        given()
                .spec(userWithRole.get(ROLE_USER))
                .contentType(ContentType.JSON)
                .body(ImmutableMap.of("distanceMeters", ANY_DISTANCE + 1))
        .when()
                .patch(pathToEntry)
        .then()
                .log().all()
                .statusCode(200)
        ;

        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
                .get(pathToEntry)
        .then()
                .statusCode(200)
                .body("distanceMeters", equalTo(ANY_DISTANCE + 1))
                .body("timeSeconds", equalTo(ANY_DURRATION))
                .body("userName", equalTo(ROLE_USER.name()))
        ;
    }

    @Test
    public void userCanDelete() {
        String pathToEntry = createJogEntries(ROLE_USER);

        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
                .delete(pathToEntry)
        .then()
                .log().all()
                .statusCode(204)
        ;

        given()
                .spec(userWithRole.get(ROLE_USER))
                .contentType(ContentType.JSON)
        .when()
                .get(pathToEntry)
        .then()
                .log().all()
                .statusCode(404)
        ;
    }

    @Test
    public void contentManagerCanDelete() {
        String pathToEntry = createJogEntries(ROLE_USER);

        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
        .when()
                .delete(pathToEntry)
        .then()
                .log().all()
                .statusCode(204)
        ;

        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
        .when()
                .get(pathToEntry)
        .then()
                .statusCode(404)
        ;
    }

    @Test
    public void userCanNotDeleteOthers() {
        String pathToEntry = createJogEntries(ROLE_CONTENT_MANAGER);

        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
                .delete(pathToEntry)
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }

    @Test
    public void userCanNotAccessOthers() {
        String pathToEntry = createJogEntries(ROLE_CONTENT_MANAGER);

        given()
                .spec(userWithRole.get(ROLE_USER))
        .when()
                .get(pathToEntry)
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }

    @Test
    public void userCanNotUpdateOthers() {
        String pathToEntry = createJogEntries(ROLE_CONTENT_MANAGER);

        given()
                .spec(userWithRole.get(ROLE_USER))
                .contentType(ContentType.JSON)
                .body(ImmutableMap.of("distanceMeters", ANY_DISTANCE + 1))
        .when()
                .patch(pathToEntry)
        .then()
                .log().all()
                .spec(forbidden)
        ;
    }

    @Test
    public void contentManagerCanAccessOthers() {
        String pathToEntry = createJogEntries(ROLE_USER);

        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
        .when()
                .get(pathToEntry)
        .then()
                .log().all()
                .statusCode(200)
                .body("distanceMeters", equalTo(ANY_DISTANCE))
        ;
    }

    @Test
    public void contentManagerCanUpdateOthers() {
        String pathToEntry = createJogEntries(ROLE_USER);

        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
                .contentType(ContentType.JSON)
                .body(ImmutableMap.of("distanceMeters", ANY_DISTANCE + 1))
        .when()
                .patch(pathToEntry)
        .then()
                .log().all()
                .statusCode(200)
        ;

        given()
                .spec(userWithRole.get(ROLE_CONTENT_MANAGER))
        .when()
               .get(pathToEntry)
        .then()
                .log().all()
                .statusCode(200)
                .body("distanceMeters", equalTo(ANY_DISTANCE + 1))
        ;
    }


}
