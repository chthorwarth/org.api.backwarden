package org.backwarden.api.adapters.controller;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.CredentialCreationDTO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class CredentialControllerTest extends BaseControllerTest {

    CredentialCreationDTO getCredentialDTO() {
        CredentialCreationDTO dto = new CredentialCreationDTO();
        dto.setTitle("My Login");
        dto.setUsername("user");
        dto.setPassword("secret");
        return dto;
    }

    private long createCredential(String token, long vaultId, CredentialCreationDTO dto) {

        String location = given().auth().oauth2(token).contentType(ContentType.JSON).body(dto).when().post("/vaults/" + vaultId + "/credentials").then().statusCode(201).extract().header("Location");

        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }


    // ─────────────────────────
    // GET ALL
    // ─────────────────────────

    @Test
    void getCredentials_withOwnToken_returns200() {

        long userId = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        long credentialId = createCredential(token, vaultId, getCredentialDTO());

        List<String> linkHeaders = given().auth().oauth2(token).when().get("/vaults/" + vaultId + "/credentials").then().statusCode(200).contentType(ContentType.JSON).body("credentialDTOS[0].password", containsString(getCredentialDTO().getPassword())).body("credentialDTOS", notNullValue()).body("credentialDTOS[0].selfLink", containsString("/vaults/" + vaultId + "/credentials/" + credentialId)).body("selfLink", containsString("/vaults/" + vaultId + "/credentials")).extract().headers().getValues("Link");

        Assertions.assertTrue(linkHeaders.stream().anyMatch(h -> h.contains("rel=\"createCredential\"")), "Link-Header muss rel=\"createCredential\" enthalten");
        Assertions.assertTrue(linkHeaders.stream().anyMatch(h -> h.contains("/vaults/" + vaultId + "/credentials")), "Link-Header muss Credential-Collection enthalten");
    }

    @Test
    void getCredentials_withForeignToken_returns403() {

        long user1 = register("me@test.de", "Strong#12345");
        String token1 = token("me@test.de", "Strong#12345");

        long vaultId = createVault(token1, user1);

        long user2 = register("other@test.de", "Strong#12345");
        String token2 = token("other@test.de", "Strong#12345");

        given().auth().oauth2(token2).when().get("/vaults/" + vaultId + "/credentials").then().statusCode(403);
    }


    // ─────────────────────────
    // CREATE
    // ─────────────────────────

    @Test
    void createCredential_success_returns201_andLocation() {

        long userId = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");
        long vaultId = createVault(token, userId);

        CredentialCreationDTO dto = new CredentialCreationDTO();
        dto.setTitle("My Site");
        dto.setUsername("me");
        dto.setPassword("pw");

        given().auth().oauth2(token).contentType(ContentType.JSON).body(dto).when().post("/vaults/" + vaultId + "/credentials").then().statusCode(201).header("Location", matchesPattern(".*/vaults/" + vaultId + "/credentials/[0-9]+$"));
    }

    @Test
    void createCredential_onForeignVault_returns403() {

        long uid1 = register("a@test.de", "Strong#12345");
        String token1 = token("a@test.de", "Strong#12345");

        long vaultId = createVault(token1, uid1);

        long uid2 = register("b@test.de", "Strong#12345");
        String token2 = token("b@test.de", "Strong#12345");

        CredentialCreationDTO dto = new CredentialCreationDTO();
        dto.setTitle("X");
        dto.setUsername("x");
        dto.setPassword("x");

        given().auth().oauth2(token2).contentType(ContentType.JSON).body(dto).when().post("/vaults/" + vaultId + "/credentials").then().statusCode(403);
    }


    // ─────────────────────────
    // GET SINGLE
    // ─────────────────────────

    @Test
    void getSingleCredential_withOwnToken_returns200_andSelfLink() {

        long userId = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");
        long vaultId = createVault(token, userId);
        long credId = createCredential(token, vaultId, getCredentialDTO());

        List<String> linkHeaders =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get("/vaults/" + vaultId + "/credentials/" + credId)
                        .then()
                        .statusCode(200)
                        .body(containsString(getCredentialDTO().getPassword()))
                        .body("selfLink",
                                containsString("/vaults/" + vaultId + "/credentials/" + credId))
                        .extract()
                        .headers()
                        .getValues("Link");

        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("rel=\"deleteCredential\""))
        );
    }

    @Test
    void getSingleCredential_withForeignToken_returns403() {

        long u1 = register("a@test.de", "Strong#12345");
        String t1 = token("a@test.de", "Strong#12345");
        long vaultId = createVault(t1, u1);
        long credId = createCredential(t1, vaultId, getCredentialDTO());

        long u2 = register("b@test.de", "Strong#12345");
        String t2 = token("b@test.de", "Strong#12345");

        given().auth().oauth2(t2).when().get("/vaults/" + vaultId + "/credentials/" + credId).then().statusCode(403);
    }


    // ─────────────────────────
    // DELETE
    // ─────────────────────────

    @Test
    void deleteCredential_withOwnToken_returns204_andLinkHeader() {

        long userId = register("me@test.de", "Strong#12345");
        String token = token("me@test.de", "Strong#12345");
        long vaultId = createVault(token, userId);
        long credId = createCredential(token, vaultId, getCredentialDTO());

        List<String> linkHeaders =
                given()
                        .auth().oauth2(token)
                        .when()
                        .delete("/vaults/" + vaultId + "/credentials/" + credId)
                        .then()
                        .statusCode(204)
                        .extract()
                        .headers()
                        .getValues("Link");

        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("rel=\"getAllCredentials\"")),
                "Link-Header muss rel=\"getAllCredentials\" enthalten"
        );
        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("/vaults/" + vaultId)),
                "Link-Header muss Vault-Link enthalten"
        );
    }

    @Test
    void deleteCredential_withForeignToken_returns403() {

        long u1 = register("a@test.de", "Strong#12345");
        String t1 = token("a@test.de", "Strong#12345");
        long vaultId = createVault(t1, u1);
        long credId = createCredential(t1, vaultId, getCredentialDTO());

        long u2 = register("b@test.de", "Strong#12345");
        String t2 = token("b@test.de", "Strong#12345");

        given().auth().oauth2(t2).when().delete("/vaults/" + vaultId + "/credentials/" + credId).then().statusCode(403);
    }
}