package org.backwarden.api.adapters.controller.model.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import org.backwarden.api.adapters.controller.model.UserDTO;
import org.openapitools.model.UsersPostRequest;
import org.openapitools.model.UsersUserIdGet200Response;

/**
 * Represents a collection of functions to interact with the API endpoints.
 */

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-12-13T14:49:05.968114+01:00[Europe/Berlin]", comments = "Generator version: 7.17.0")
@Path("/users")
public interface UsersApi {

    /**
     * @param usersPostRequest
     * @return Created
     */
    @POST
    @Consumes({"application/json"})
    void usersPost(@Valid @NotNull UserDTO usersPostRequest);


    /**
     * @param userId
     * @return OK
     * @return User not found
     */
    @GET
    @Path("/{userId}")
    @Produces({"application/json"})
    UserDTO usersUserIdGet(@PathParam("userId") Integer userId);

}
