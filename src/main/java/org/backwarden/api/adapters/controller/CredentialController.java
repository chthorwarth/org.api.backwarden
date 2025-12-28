package org.backwarden.api.adapters.controller;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.backwarden.api.adapters.controller.model.converter.CredentialDTOConverter;
import org.backwarden.api.logic.ports.input.CredentialUseCase;

import org.openapitools.api.CredentialsApi;

import org.openapitools.model.CredentialDTO;
import org.openapitools.model.CredentialWrapperDTO;

import java.net.URI;
import java.util.List;

@ApplicationScoped
public class CredentialController implements CredentialsApi {

    @Inject
    CredentialUseCase credentialService;

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdDelete(Integer vaultId, Integer credentialId) {
        credentialService.deleteCredential(credentialId);
        return Response.ok().build();
    }

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdGet(Integer vaultId, Integer credentialId) {
        CredentialDTO credentialDTO = CredentialDTOConverter.toDTO(credentialService.getCredential(credentialId));
        return Response.ok(credentialDTO).build();
    }

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdPut(Integer vaultId, Integer credentialId, CredentialDTO credentialDTO) {
        credentialService.updateCredential(credentialId, CredentialDTOConverter.fromDTO(credentialDTO));
        return Response.ok().build();
    }

    @Override
    public Response vaultsVaultIdCredentialsGet(Integer vaultId) {
        System.out.println("Test\n\n\n\n");
        CredentialWrapperDTO wrapperDTO = new CredentialWrapperDTO();

        List<CredentialDTO> credentialDTOs = CredentialDTOConverter.toDTOList(credentialService.getAllCredentials(vaultId));

        for (CredentialDTO dto : credentialDTOs)
            dto.setSelfLink(URI.create("/vaults/" + vaultId + "/credentials/" + dto.getId()));


        wrapperDTO.credentialDTOS(credentialDTOs);
        wrapperDTO.setSelfLink(
                URI.create("/vaults/" + vaultId + "/credentials/")
        );
        //return Response.ok(wrapperDTO).header("Link", "<http://localhost:8085/vaults/{vaultId}/credentials/{credentialId}>; rel=\"item\"; type=\"application/json\"").build();
        return Response.ok(wrapperDTO).build();
    }

    @Override
    public Response vaultsVaultIdCredentialsPost(Integer vaultId, CredentialDTO credentialDTO)
    {
        credentialService.createCredentials(CredentialDTOConverter.fromDTO(credentialDTO));
        return Response.ok().build();
    }

}