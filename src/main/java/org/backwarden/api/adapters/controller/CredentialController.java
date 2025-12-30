package org.backwarden.api.adapters.controller;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;


import org.openapitools.api.CredentialsApi;
import org.openapitools.model.CredentialCreationDTO;
import org.backwarden.api.adapters.controller.model.converter.CredentialDTOConverter;
import org.backwarden.api.logic.ports.input.CredentialUseCase;

import org.openapitools.api.CredentialsApi;

import org.openapitools.model.CredentialDTO;
import org.openapitools.model.CredentialUpdateDTO;
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
    public Response vaultsVaultIdCredentialsCredentialIdPut(Integer vaultId, Integer credentialId, CredentialUpdateDTO credentialUpdateDTO) {
        credentialService.updateCredential(credentialId, CredentialDTOConverter.fromDTO(credentialUpdateDTO));
        return Response.ok().build();
    }

    @Override
    public Response vaultsVaultIdCredentialsGet(Integer vaultId) {
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
    public Response vaultsVaultIdCredentialsPost(Integer vaultId, CredentialCreationDTO credentialCreationDTO) {
        credentialService.createCredentials(CredentialDTOConverter.fromDTO(credentialCreationDTO), vaultId);
        return Response.ok().build();
    }

}