package org.backwarden.api.adapters.controller.model.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import org.backwarden.api.adapters.controller.model.VaultDTO;

/**
 * Represents a collection of functions to interact with the API endpoints.
 */

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-12-13T14:49:05.968114+01:00[Europe/Berlin]", comments = "Generator version: 7.17.0")
@Path("/vaults")
public interface VaultApi
{

    /**
     * @param vaultDTO
     * @return Created
     */
    @POST
    @Consumes({"application/json"})
    void vaultsPost(@Valid @NotNull VaultDTO vaultDTO);


    /**
     * @param vaultId
     * @return OK
     * @return User not found
     */
    @GET
    @Path("/{vaultId}")
    @Produces({"application/json"})
    VaultDTO vaultsVaultIdGet(@PathParam("vaultId") Integer vaultId);

}
