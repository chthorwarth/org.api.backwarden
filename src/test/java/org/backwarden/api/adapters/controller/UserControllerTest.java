package org.backwarden.api.adapters.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class UserControllerTest {

    @Test
    void testPostUsersEndpoint() {
        UserDTO userDTO = new UserDTO();
        userDTO.masterEmail("bla.bla@bla.bla");
        userDTO.masterPassword("MeinBla#123Bla");
        userDTO.failedLoginAttempts(0);
        given()
                .contentType("application/json")
                .body(userDTO)
                .when().post("/users")
                .then()
                .statusCode(201);
    }
}
