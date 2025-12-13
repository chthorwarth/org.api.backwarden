package org.backwarden.api.adapters.controller.model.interfaces;


import org.backwarden.api.adapters.controller.model.CredentialDTO;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

/**
 * Represents a collection of functions to interact with the API endpoints.
 */

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-12-13T15:14:28.040174+01:00[Europe/Berlin]", comments = "Generator version: 7.17.0")
public interface CredentialsApi {

    /**
     * @param vaultId
     * @return OK
     */
    @GET
    @Path("/vaults/{vaultId}/credentials")
    @Produces({"application/json"})
    List<CredentialDTO> vaultsVaultIdCredentialsGet(@PathParam("vaultId") Integer vaultId);


    /**
     * @param vaultId
     * @param vaultsVaultIdCredentialsPostRequest
     * @return Created
     */
    @POST
    @Path("/vaults/{vaultId}/credentials")
    @Consumes({"application/json"})
    void vaultsVaultIdCredentialsPost(@PathParam("vaultId") Integer vaultId, @Valid @NotNull CredentialDTO vaultsVaultIdCredentialsPostRequest);

}
