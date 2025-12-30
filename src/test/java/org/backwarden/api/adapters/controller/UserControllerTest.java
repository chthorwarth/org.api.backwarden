package org.backwarden.api.adapters.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserRegistrationDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class UserControllerTest {

    @Test
    void testPostUsersEndpoint() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.masterEmail("bla.bla@bla.bla");
        userDTO.masterPassword("MeinBla#123Bla");
        given()
                .contentType("application/json")
                .body(userDTO)
                .when().post("/users")
                .then()
                .statusCode(201);
    }
}
