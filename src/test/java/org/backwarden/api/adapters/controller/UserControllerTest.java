package org.backwarden.api.adapters.controller;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.UserAdapter;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserRegistrationDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class UserControllerTest extends BaseControllerTest {

    @Test
    void registerAccount_success_returns201() {

        UserRegistrationDTO req = new UserRegistrationDTO();
        req.masterEmail("unique@test.de");
        req.masterPassword("Strong#12345");

        given()
                .contentType("application/json")
                .body(req)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .header("Location", matchesPattern(".*/users/[0-9]+$"))
                .header("Link", containsString("rel=\"generateToken\""))
                .header("Link", containsString("/token"));
    }

    @Test
    void registerAccount_withExistingEmail_returns409() {

        UserRegistrationDTO req = new UserRegistrationDTO();
        req.masterEmail("duplicate@test.de");
        req.masterPassword("Strong#12345");

        given()
                .contentType("application/json")
                .body(req)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body(req)
                .when()
                .post("/users")
                .then()
                .statusCode(409);
    }

    @Test
    void registerAccount_withWeakPassword_returns422() {

        UserRegistrationDTO req = new UserRegistrationDTO();
        req.masterEmail("valid@test.de");
        req.masterPassword("abc"); // bewusst zu schwach

        given()
                .contentType("application/json")
                .body(req)
                .when()
                .post("/users")
                .then()
                .statusCode(400);
    }

    @Test
    void getUser_withOwnToken_returns200() {

        long id = register("me@test.de", "Strong#12345");

        String jwt = token("me@test.de", "Strong#12345");

        given()
                .auth().oauth2(jwt)
                .when()
                .get("/users/" + id)
                .then()
                .statusCode(200)
                .body("masterEmail", equalTo("me@test.de"));
    }

    @Test
    void getUser_withForeignToken_returns403() {

        // User A
        long idA = register("a@test.de", "Strong#12345");


        // User B
        register("b@test.de", "Strong#12345");
        String jwtB = token("b@test.de", "Strong#12345");

        // B versucht A zu lesen
        given()
                .auth().oauth2(jwtB)
                .when()
                .get("/users/" + idA)
                .then()
                .statusCode(403);
    }
}
