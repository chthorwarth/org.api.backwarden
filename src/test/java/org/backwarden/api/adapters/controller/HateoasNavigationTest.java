package org.backwarden.api.adapters.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.mapper.ObjectMapper;
import org.backwarden.api.adapters.controller.model.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class HateoasNavigationTest extends BaseControllerTest {

    private static final Pattern LINK_PATTERN =
            Pattern.compile("<([^>]+)>;\\s*rel=\"([^\"]+)\"");

    private String extractLink(Headers headers, String rel) {

        Pattern p = Pattern.compile("<([^>]+)>;\\s*rel=\"([^\"]+)\"");

        for (Header h : headers) {
            if (!h.getName().equalsIgnoreCase("Link")) continue;

            Matcher m = p.matcher(h.getValue());
            while (m.find()) {
                if (m.group(2).equals(rel)) {
                    return m.group(1);
                }
            }
        }

        throw new AssertionError("Rel not found: " + rel + " in headers: " + headers);
    }

    @Test
    void fullHateoasNavigation_WorksUsingOnlyLinks() {

        // GET /api (Entry point)
        Headers api =
                given()
                        .when()
                        .get("/api")
                        .then()
                        .statusCode(200)
                        .extract()
                        .headers();

        String registerLink = extractLink(api, "registerUser");
        String gentokenLink = extractLink(api, "generateToken");
        Assertions.assertNotNull(gentokenLink);

        // POST /users  (Register)
        UserRegistrationDTO reg = new UserRegistrationDTO();
        reg.setMasterEmail("me@test.de");
        reg.setMasterPassword("Strong#12345");

        var registerResponse =
                given()
                        .contentType(ContentType.JSON)
                        .body(reg)
                        .when()
                        .post(registerLink)
                        .then()
                        .statusCode(201)
                        .extract();

        String userLocation = registerResponse.header("Location");
        String tokenLink = extractLink(registerResponse.headers(), "generateToken");

        // POST /token (Login)
        LoginRequest login = new LoginRequest();
        login.email = "me@test.de";
        login.password = "Strong#12345";

        var tokenResponse =
                given()
                        .contentType(ContentType.JSON)
                        .body(login)
                        .when()
                        .post(tokenLink)
                        .then()
                        .statusCode(201)
                        .extract();

        String token = tokenResponse.body().asString();
        String vaultCollectionLink = extractLink(tokenResponse.headers(), "getAllVaults");

        // GET /users/{id}/vaults (Follow link!)
        var vaultListResponse =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get(vaultCollectionLink)
                        .then()
                        .statusCode(200)
                        .body("selfLink", containsString("/vaults"))
                        .extract();

        String createVaultLink = extractLink(vaultListResponse.headers(), "createVault");

        // POST vault (Follow link!)
        VaultCreationDTO vault = new VaultCreationDTO();
        vault.setTitle("My Vault");
        vault.setAutoFill(true);

        String vaultLocation =
                given()
                        .auth().oauth2(token)
                        .contentType(ContentType.JSON)
                        .body(vault)
                        .when()
                        .post(createVaultLink)
                        .then()
                        .statusCode(201)
                        .extract()
                        .header("Location");

        // GET vault (Follow Location!)
        var vaultResponse =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get(vaultLocation)
                        .then()
                        .statusCode(200)
                        .extract();

        String showAllVaults = extractLink(vaultResponse.headers(), "getAllVaults");
        String getAllCredentialsLink = extractLink(vaultResponse.headers(), "getAllCredentials");
        String deleteVaultLink = extractLink(vaultResponse.headers(), "deleteVault");
        String updateVaultLink = extractLink(vaultResponse.headers(), "updateVault");
        Assertions.assertNotNull(deleteVaultLink);
        Assertions.assertNotNull(showAllVaults);
        Assertions.assertNotNull(getAllCredentialsLink);
        Assertions.assertNotNull(updateVaultLink);

        var credListResponse =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get(getAllCredentialsLink)
                        .then()
                        .statusCode(200)
                        .extract();
        String createCredentialLink = extractLink(credListResponse.headers(), "createCredential");

        // PUT vault (Follow link!)
        VaultUpdateDTO vaultUpdate = new VaultUpdateDTO();
        vaultUpdate.setTitle("My Vault Updated");
        vaultUpdate.setAutoFill(false);
        vaultUpdate.setId(1L);

        var vaultUpdateResponse = given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(vaultUpdate)
                .when()
                .put(updateVaultLink)
                .then()
                .statusCode(204)
                .extract();

        String singleCredentialLink = extractLink(credListResponse.headers(), "getOneVault");
        Assertions.assertNotNull(singleCredentialLink);

        // POST credential  (follow link!)
        CredentialCreationDTO cred = new CredentialCreationDTO();
        cred.setTitle("My Login");
        cred.setUsername("me");
        cred.setPassword("pw");

        String credentialLocation =
                given()
                        .auth().oauth2(token)
                        .contentType(ContentType.JSON)
                        .body(cred)
                        .when()
                        .post(createCredentialLink)
                        .then()
                        .statusCode(201)
                        .extract()
                        .header("Location");

        // GET credential (follow link)
        var credResponse =
                given()
                        .auth().oauth2(token)
                        .when()
                        .get(credentialLocation)
                        .then()
                        .statusCode(200)
                        .extract();

        String deleteCredentialLink = extractLink(credResponse.headers(), "deleteCredential");
        String getAllCredentialsLink2 = extractLink(credResponse.headers(), "getAllCredentials");
        Assertions.assertNotNull(getAllCredentialsLink2);


        // DELETE credential
        given()
                .auth().oauth2(token)
                .when()
                .delete(deleteCredentialLink)
                .then()
                .statusCode(204);

        // DELETE vault
        given()
                .auth().oauth2(token)
                .when()
                .delete(deleteVaultLink)
                .then()
                .statusCode(204);
    }
}

