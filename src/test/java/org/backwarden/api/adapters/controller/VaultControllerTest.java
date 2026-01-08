package org.backwarden.api.adapters.controller;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.UserAdapter;
import org.backwarden.api.adapters.database.VaultAdapter;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.logic.ports.output.persistence.UserRepository;
import org.backwarden.api.logic.ports.output.persistence.VaultRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserRegistrationDTO;
import org.openapitools.model.VaultCreationDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class VaultControllerTest extends BaseControllerTest {

    @Test
    void getVaults_withOwnToken_returns200() {

        long userid = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");

        long vaultid = createVault(token, userid);

        List<String> linkHeaders =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get("/users/" + userid + "/vaults")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("vaultDTOS", notNullValue())
                        .body("selfLink", containsString("/users/" + userid + "/vaults"))
                        .body("vaultDTOS[0].selfLink",
                                containsString("/users/" + userid + "/vaults/" + vaultid))
                        .extract()
                        .headers()
                        .getValues("Link");

        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("rel=\"createVault\"")),
                "Link-Header muss rel=\"createVault\" enthalten"
        );
        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("/users/" + userid + "/vaults")),
                "Link-Header muss Vault-Collection enthalten"
        );
    }

    @Test
    void getVaults_withForeignToken_returns403() {

        long userid = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");

        given()
                .auth().oauth2(token)
                .when()
                .get("/users/9999999/vaults")
                .then()
                .statusCode(403);
    }

    @Test
    void createVault_success_returns201_andLocation() {

        long userid = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");

        VaultCreationDTO dto = new VaultCreationDTO();
        dto.setTitle("My Vault");
        dto.setAutoFill(true);

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/users/" + userid + "/vaults")
                .then()
                .statusCode(201)
                .header("Location", matchesPattern(".*/users/" + userid + "/vaults/[0-9]+$"));
    }

    @Test
    void createVault_withForeignUser_returns403() {

        long userid = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");

        VaultCreationDTO dto = new VaultCreationDTO();
        dto.setTitle("X");
        dto.setAutoFill(false);

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/users/999999999/vaults")
                .then()
                .statusCode(403);
    }

    @Test
    void deleteVault_withOwnToken_returns200() {

        long userid = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");

        // Vault erstellen
        VaultCreationDTO dto = new VaultCreationDTO();
        dto.setTitle("Delete me");
        dto.setAutoFill(false);

        long vaultid = createVault(token, userid);

        List<String> linkHeaders =
                given()
                        .auth().oauth2(token)
                        .when()
                        .delete("/users/" + userid + "/vaults/" + vaultid)
                        .then()
                        .statusCode(204)
                        .extract()
                        .headers()
                        .getValues("Link");

        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("rel=\"getAllVaults\"")),
                "Link-Header muss rel=\"getAllVaults\" enthalten"
        );
        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("/users/" + userid + "/vaults")),
                "Link-Header muss Vault-Collection enthalten"
        );
    }

    @Test
    void getSingleVault_withOwnToken_returns200_andSelfLink() {

        long userid = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");

        Map<String, String> map = new HashMap<>();

        long vaultid = createVault(token, userid);

        List<String> linkheaders = given()
                .auth().oauth2(token)
                .when()
                .get("/users/" + userid + "/vaults/" + vaultid)
                .then()
                .log().headers()
                .statusCode(200)
                .body("selfLink", containsString("/users/" + userid + "/vaults/" + vaultid))
                .extract().headers().getValues("Link");
        Assertions.assertTrue(linkheaders.stream().anyMatch(s -> s.contains("rel=\"deleteVault\"")));
        Assertions.assertTrue(linkheaders.stream().anyMatch(s -> s.contains("/users/" + userid + "/vaults/" + vaultid)));
    }

    @Test
    void getSingleVault_withForeignToken_returns403() {

        long userid1 = register("me@test.de", "Strong#12345");
        String token1 = token("me@test.de", "Strong#12345");

        long userid2 = register("other@test.de", "Strong#12345");
        String token2 = token("other@test.de", "Strong#12345");

        VaultCreationDTO dto = new VaultCreationDTO();
        dto.setTitle("Not yours");
        dto.setAutoFill(false);

        String location =
                given()
                        .auth().oauth2(token1)
                        .contentType(ContentType.JSON)
                        .body(dto)
                        .when()
                        .post("/users/" + userid1 + "/vaults")
                        .then()
                        .statusCode(201)
                        .extract()
                        .header("Location");

        given()
                .auth().oauth2(token2)
                .when()
                .get(location)
                .then()
                .statusCode(403);
    }
}
