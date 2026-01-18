package org.backwarden.api.adapters.controller;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.CredentialCreationDTO;
import org.openapitools.model.CredentialUpdateDTO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class CredentialControllerTest extends BaseControllerTest {


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
        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("rel=\"getAllCredentials\""))
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

    @Test
    void getSingleCredential_withMatchingEtag_returns304() {

        long userId = register("etagcred@test.de", "Strong#12345");
        String token = token("etagcred@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        long credId = createCredential(token, vaultId, getCredentialDTO());

        // Erster Request → ETag holen
        String etag =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get("/vaults/" + vaultId + "/credentials/" + credId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .header("ETag");

        // Conditional GET mit If-None-Match
        given()
                .auth().oauth2(token)
                .header("If-None-Match", etag)
                .when()
                .get("/vaults/" + vaultId + "/credentials/" + credId)
                .then()
                .statusCode(304)
                .body(is(emptyOrNullString()));
    }

    @Test
    void getSingleCredential_withNonMatchingEtag_returns200() {

        long userId = register("etagcred2@test.de", "Strong#12345");
        String token = token("etagcred2@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        long credId = createCredential(token, vaultId, getCredentialDTO());

        given()
                .auth().oauth2(token)
                .header("If-None-Match", "\"wrong-etag\"")
                .when()
                .get("/vaults/" + vaultId + "/credentials/" + credId)
                .then()
                .statusCode(200)
                .body(containsString(getCredentialDTO().getPassword()));
    }

    @Test
    void getAllCredentials_firstRequest_returns200_andEtag() {

        long userId = register("credwrap1@test.de", "Strong#12345");
        String token = token("credwrap1@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        createCredential(token, vaultId, getCredentialDTO());

        given()
                .auth().oauth2(token)
                .when()
                .get("/vaults/" + vaultId + "/credentials")
                .then()
                .statusCode(200)
                .header("ETag", notNullValue())
                .body("credentialDTOS.size()", greaterThan(0));
    }

    @Test
    void getAllCredentials_withMatchingEtag_returns304() {

        long userId = register("credwrap2@test.de", "Strong#12345");
        String token = token("credwrap2@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        createCredential(token, vaultId, getCredentialDTO());

        // First GET → get ETag
        String etag =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get("/vaults/" + vaultId + "/credentials")
                        .then()
                        .statusCode(200)
                        .extract()
                        .header("ETag");

        // Conditional GET
        given()
                .auth().oauth2(token)
                .header("If-None-Match", etag)
                .when()
                .get("/vaults/" + vaultId + "/credentials")
                .then()
                .statusCode(304)
                .body(is(emptyOrNullString()));
    }

    @Test
    void getAllCredentials_withWrongEtag_returns200() {

        long userId = register("credwrap3@test.de", "Strong#12345");
        String token = token("credwrap3@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        createCredential(token, vaultId, getCredentialDTO());

        given()
                .auth().oauth2(token)
                .header("If-None-Match", "\"wrong-etag\"")
                .when()
                .get("/vaults/" + vaultId + "/credentials")
                .then()
                .statusCode(200)
                .body("credentialDTOS.size()", greaterThan(0));
    }


    @Test
    void getAllCredentials_afterCreatingCredential_etagChanges() {

        long userId = register("credwrap4@test.de", "Strong#12345");
        String token = token("credwrap4@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);

        String etagBefore =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get("/vaults/" + vaultId + "/credentials")
                        .then()
                        .statusCode(200)
                        .extract()
                        .header("ETag");

        createCredential(token, vaultId, getCredentialDTO());

        String etagAfter =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get("/vaults/" + vaultId + "/credentials")
                        .then()
                        .statusCode(200)
                        .extract()
                        .header("ETag");

        Assertions.assertNotEquals(etagBefore, etagAfter);
    }

    @Test
    void deleteCredential_withMatchingEtag_returns204() {

        long userId = register("cdel2@test.de", "Strong#12345");
        String token = token("cdel2@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        long credId = createCredential(token, vaultId, getCredentialDTO());

        String etag =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get("/vaults/" + vaultId + "/credentials/" + credId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .header("ETag");

        given()
                .auth().oauth2(token)
                .header("If-Match", etag)
                .when()
                .delete("/vaults/" + vaultId + "/credentials/" + credId)
                .then()
                .statusCode(204);
    }

    @Test
    void deleteCredential_withNonMatchingEtag_returns412() {

        long userId = register("cdel3@test.de", "Strong#12345");
        String token = token("cdel3@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        long credId = createCredential(token, vaultId, getCredentialDTO());

        given()
                .auth().oauth2(token)
                .header("If-Match", "\"wrong-etag\"")
                .when()
                .delete("/vaults/" + vaultId + "/credentials/" + credId)
                .then()
                .statusCode(412);
    }


    // ─────────────────────────
// UPDATE (PUT)
// ─────────────────────────

    @Test
    void putCredential_withNonMatchingEtag_returns412() {

        long userId = register("put-cred1@test.de", "Strong#12345");
        String token = token("put-cred1@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        long credentialId = createCredential(token, vaultId, getCredentialDTO());

        CredentialUpdateDTO updateDTO = new CredentialUpdateDTO();
        updateDTO.setId(credentialId);
        updateDTO.setTitle("Should Not Update");
        updateDTO.setUsername("nope");
        updateDTO.setPassword("nope");

        given()
                .auth().oauth2(token)
                .header("If-Match", "\"wrong-etag\"")
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/vaults/" + vaultId + "/credentials/" + credentialId)
                .then()
                .statusCode(412);
    }

    @Test
    void putCredential_withMatchingEtag_returns204_andLinkHeader() {

        long userId = register("put-cred2@test.de", "Strong#12345");
        String token = token("put-cred2@test.de", "Strong#12345");

        long vaultId = createVault(token, userId);
        long credentialId = createCredential(token, vaultId, getCredentialDTO());

        // ETag vom GET holen
        String etag =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get("/vaults/" + vaultId + "/credentials/" + credentialId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .header("ETag");

        CredentialUpdateDTO updateDTO = new CredentialUpdateDTO();
        updateDTO.setId(credentialId);
        updateDTO.setTitle("Updated Title");
        updateDTO.setUsername("updatedUser");
        updateDTO.setPassword("updatedPw");

        List<String> linkHeaders =
                given()
                        .auth().oauth2(token)
                        .header("If-Match", etag)
                        .contentType(ContentType.JSON)
                        .body(updateDTO)
                        .when()
                        .put("/vaults/" + vaultId + "/credentials/" + credentialId)
                        .then()
                        .statusCode(204)
                        // du baust: Response.noContent().link(getOneCredential(...), relNameGetOneCredential)...
                        .extract()
                        .headers()
                        .getValues("Link");

        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("rel=\"getOneCredential\"")),
                "Link-Header muss rel=\"getOneCredential\" enthalten"
        );
        Assertions.assertTrue(
                linkHeaders.stream().anyMatch(h -> h.contains("/vaults/" + vaultId + "/credentials/" + credentialId)),
                "Link-Header muss auf das Credential-Self zeigen"
        );
    }

    @Test
    void putCredential_onForeignVault_returns403() {

        long uid1 = register("put-cred3-a@test.de", "Strong#12345");
        String token1 = token("put-cred3-a@test.de", "Strong#12345");

        long vaultId = createVault(token1, uid1);
        long credentialId = createCredential(token1, vaultId, getCredentialDTO());

        long uid2 = register("put-cred3-b@test.de", "Strong#12345");
        String token2 = token("put-cred3-b@test.de", "Strong#12345");

        CredentialUpdateDTO updateDTO = new CredentialUpdateDTO();
        updateDTO.setId(credentialId);
        updateDTO.setTitle("Should Not Update");
        updateDTO.setUsername("nope");
        updateDTO.setPassword("nope");

        given()
                .auth().oauth2(token2)
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/vaults/" + vaultId + "/credentials/" + credentialId)
                .then()
                .statusCode(403);
    }

}