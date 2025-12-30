package org.backwarden.api.adapters.controller;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;


import org.openapitools.api.CredentialsApi;
import org.openapitools.model.CredentialCreationDTO;
import org.openapitools.model.CredentialDTO;
import org.openapitools.model.CredentialUpdateDTO;
import org.openapitools.model.CredentialWrapperDTO;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CredentialController implements CredentialsApi {


    @Override
    public Response vaultsVaultIdCredentialsCredentialIdDelete(Integer vaultId, Integer credentialId) {
        System.out.printf("Deleted credential");
        return Response.ok().build();
    }

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdGet(Integer vaultId, Integer credentialId) {
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.id(1L);
        credentialDTO.setTitle("Credential");
        return Response.ok(credentialDTO).build();
    }

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdPut(Integer vaultId, Integer credentialId, CredentialUpdateDTO credentialUpdateDTO) {
        credentialUpdateDTO.id(1L);
        credentialUpdateDTO.setTitle("CredentialVault");
        return Response.ok().build();
    }

    @Override
    public Response vaultsVaultIdCredentialsGet(Integer vaultId) {
        CredentialWrapperDTO wrapperDTO = new CredentialWrapperDTO();
        List<CredentialDTO> credentialDTOs = new ArrayList<>();
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.id(1L);
        credentialDTO.setTitle("Credential");
        credentialDTOs.add(credentialDTO);
        credentialDTO.setSelfLink(
                URI.create("/vaults/" + vaultId + "/credentials/" + credentialDTO.getId())
        );
        wrapperDTO.credentialDTOS(credentialDTOs);
        wrapperDTO.setSelfLink(
                URI.create("/vaults/" + vaultId + "/credentials/")
        );
        return Response.ok(wrapperDTO).header("Link", "<http://localhost:8080/vaults/{vaultId}/credentials/{credentialId}>; rel=\"item\"; type=\"application/json\"").build();
    }

    @Override
    public Response vaultsVaultIdCredentialsPost(Integer vaultId, CredentialCreationDTO credentialCreationDTO) {
        System.out.printf(credentialCreationDTO.getTitle());
        return Response.ok().build();
    }

}