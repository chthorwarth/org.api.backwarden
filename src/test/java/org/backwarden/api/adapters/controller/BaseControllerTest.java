package org.backwarden.api.adapters.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.logic.ports.output.persistence.UserRepository;
import org.backwarden.api.logic.ports.output.persistence.VaultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.openapitools.model.UserRegistrationDTO;
import org.openapitools.model.VaultCreationDTO;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BaseControllerTest {

    @Inject
    UserRepository userRepository;

    @Inject
    VaultRepository vaultRepository;

    @BeforeEach
    @Transactional
    void cleanDB() {
        vaultRepository.deleteAll();
        userRepository.deleteAll();
    }


    long register(String email, String password) {
        UserRegistrationDTO req = new UserRegistrationDTO()
                .masterEmail(email)
                .masterPassword(password);

        String location = given()
                .contentType("application/json")
                .body(req)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");   // -> /users/{id}
        String id = location.substring(location.lastIndexOf('/') + 1);
        return Long.parseLong(id);
    }

    String token(String email, String password) {

        var login = new org.backwarden.api.adapters.controller.model.LoginRequest();
        login.email = email;
        login.password = password;

        return given()
                .contentType("application/json")
                .body(login)
                .when()
                .post("/token")
                .then()
                .statusCode(201)
                .extract()
                .asString();
    }

    long createVault(String token, long userid) {
        VaultCreationDTO dto = new VaultCreationDTO();
        dto.setTitle("My Vault");
        dto.setAutoFill(true);
        String location = given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/users/" + userid + "/vaults")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        System.out.println(location);
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }
}
