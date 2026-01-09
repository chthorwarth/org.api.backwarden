package org.backwarden.api.adapters.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserRegistrationDTO;
import org.openapitools.model.VaultCreationDTO;
import org.openapitools.model.VaultUpdateDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class CacheTest extends BaseControllerTest {

    @Test
    void apiEntryPoint_hasPublicValidationCacheHeaders() {

        given()
                .when()
                .get("/api")
                .then()
                .statusCode(200)
                .header("ETag", notNullValue())
                .header("Cache-Control", allOf(
                        containsString("no-cache"),
                        containsString("must-revalidate")
                ));
    }

    @Test
    void postUsers_isNotCacheable() {

        UserRegistrationDTO req = new UserRegistrationDTO();
        req.masterEmail("cache1@test.de");
        req.masterPassword("Strong#12345");

        given()
                .contentType("application/json")
                .body(req)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .header("Cache-Control", containsString("no-store"));
    }

    @Test
    void getUser_hasPrivateValidationCacheHeaders() {

        long userId = register("cache2@test.de", "Strong#12345");
        String token = token("cache2@test.de", "Strong#12345");

        given()
                .auth().oauth2(token)
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .header("ETag", notNullValue())
                .header("Cache-Control", allOf(
                        containsString("private"),
                        containsString("no-cache"),
                        containsString("must-revalidate")
                ));
    }

    @Test
    void getVaultList_hasPrivateValidationCacheHeaders() {

        long userId = register("cache3@test.de", "Strong#12345");
        String token = token("cache3@test.de", "Strong#12345");

        given()
                .auth().oauth2(token)
                .when()
                .get("/users/" + userId + "/vaults")
                .then()
                .statusCode(200)
                .header("ETag", notNullValue())
                .header("Cache-Control", allOf(
                        containsString("private"),
                        containsString("no-cache"),
                        containsString("must-revalidate")
                ));
    }

    @Test
    void createVault_isNotCacheable() {

        long userId = register("cache4@test.de", "Strong#12345");
        String token = token("cache4@test.de", "Strong#12345");

        VaultCreationDTO dto = new VaultCreationDTO();
        dto.title("test");
        dto.autoFill(true);

        given()
                .auth().oauth2(token)
                .contentType("application/json")
                .body(dto)
                .when()
                .post("/users/" + userId + "/vaults")
                .then()
                .statusCode(201)
                .header("Cache-Control", containsString("no-store"));
    }

    @Test
    void getVault_hasPrivateValidationCacheHeaders() {

        long userId = register("cache5@test.de", "Strong#12345");
        String token = token("cache5@test.de", "Strong#12345");
        long vaultId = createVault(token, userId);

        given()
                .auth().oauth2(token)
                .when()
                .get("/users/" + userId + "/vaults/" + vaultId)
                .then()
                .statusCode(200)
                .header("ETag", notNullValue())
                .header("Cache-Control", allOf(
                        containsString("private"),
                        containsString("no-cache"),
                        containsString("must-revalidate")
                ));
    }

    @Test
    void updateVault_isNotCacheable() {

        long userId = register("cache6@test.de", "Strong#12345");
        String token = token("cache6@test.de", "Strong#12345");
        long vaultId = createVault(token, userId);

        VaultUpdateDTO dto = new VaultUpdateDTO();
        dto.id(vaultId);
        dto.title("new");
        dto.autoFill(false);

        given()
                .auth().oauth2(token)
                .contentType("application/json")
                .body(dto)
                .when()
                .put("/users/" + userId + "/vaults/" + vaultId)
                .then()
                .statusCode(204)
                .header("Cache-Control", containsString("no-store"));
    }

    @Test
    void deleteVault_isNotCacheable() {

        long userId = register("cache7@test.de", "Strong#12345");
        String token = token("cache7@test.de", "Strong#12345");
        long vaultId = createVault(token, userId);

        String etag =
                given().auth().oauth2(token)
                        .get("/users/" + userId + "/vaults/" + vaultId)
                        .then().extract().header("ETag");

        given()
                .auth().oauth2(token)
                .header("If-Match", etag)
                .when()
                .delete("/users/" + userId + "/vaults/" + vaultId)
                .then()
                .statusCode(204)
                .header("Cache-Control", containsString("no-store"));
    }

    @Test
    void getCredentials_hasPrivateValidationCacheHeaders() {

        long userId = register("cache8@test.de", "Strong#12345");
        String token = token("cache8@test.de", "Strong#12345");
        long vaultId = createVault(token, userId);
        createCredential(token, vaultId, getCredentialDTO());

        given()
                .auth().oauth2(token)
                .when()
                .get("/vaults/" + vaultId + "/credentials")
                .then()
                .statusCode(200)
                .header("ETag", notNullValue())
                .header("Cache-Control", containsString("no-store"));
    }


}
