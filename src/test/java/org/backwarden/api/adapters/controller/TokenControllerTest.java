package org.backwarden.api.adapters.controller;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.controller.model.LoginRequest;
import org.backwarden.api.adapters.database.UserAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserRegistrationDTO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class TokenControllerTest extends BaseControllerTest {

    @Test
    void generateToken_success_returnsJwt() {

        register("login@test.de", "Strong#12345");

        LoginRequest login = new LoginRequest();
        login.email = "login@test.de";
        login.password = "Strong#12345";

        List<String> linkHeaders = given()
                .contentType("application/json")
                .body(login)
                .when()
                .post("/token")
                .then()
                .statusCode(201)
                .contentType("text/plain")
                // JWT consists from a Base64-Segment
                .body(matchesPattern("^[^.]+\\.[^.]+\\.[^.]+$"))
                .header("Location", nullValue())
                .extract()
                .headers().getValues("Link");

        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("rel=\"getAllVaults\"")),
                "Link-Header muss rel=\"createVault\" enthalten"
        );
        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.matches(".*\\/users\\/[0-9]+\\/vaults.*")),
                "Link-Header muss Vault-Collection enthalten"
        );
        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("rel=\"getOneUser\""))
        );
        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.matches(".*\\/users\\/[0-9]+.*"))
        );
    }

    @Test
    void generateToken_invalidCredentials_returns401() {

        LoginRequest login = new LoginRequest();
        login.email = "doesnotexist@test.de";
        login.password = "wrongpass";

        given()
                .contentType("application/json")
                .body(login)
                .when()
                .post("/token")
                .then()
                .statusCode(401);
    }

    @Test
    void generateToken_missingFields_returns400() {

        LoginRequest login = new LoginRequest();
        login.email = null;
        login.password = "abc";

        given()
                .contentType("application/json")
                .body(login)
                .when()
                .post("/token")
                .then()
                .statusCode(400);
    }
}