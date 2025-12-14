package org.backwarden.api.adapters.controller;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.input.CredentialAPI;
import org.backwarden.api.logic.services.CredentialService;
import org.openapitools.api.CredentialsApi;
import org.openapitools.model.CredentialDTO;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/")
public class CredentialController implements CredentialsApi {

    @Override
    public void vaultsVaultIdCredentialsCredentialIdDelete(Integer vaultId, Integer credentialId) {
        System.out.printf("Deleted credential");
    }

    @Override
    public CredentialDTO vaultsVaultIdCredentialsCredentialIdGet(Integer vaultId, Integer credentialId) {

        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.id(1L);
        credentialDTO.setTitle("Credential");
        return credentialDTO;
    }

    @Override
    public CredentialDTO vaultsVaultIdCredentialsCredentialIdPut(Integer vaultId, Integer credentialId, CredentialDTO credentialDTO) {
        credentialDTO.id(1L);
        credentialDTO.setTitle("CredentialVault");
        return credentialDTO;
    }

    @Override
    public List<CredentialDTO> vaultsVaultIdCredentialsGet(Integer vaultId) {
        List<CredentialDTO> credentialDTOs = new ArrayList<>();
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.id(1L);
        credentialDTO.setTitle("Credential");
        credentialDTOs.add(credentialDTO);
        return credentialDTOs;
    }

    @Override
    public void vaultsVaultIdCredentialsPost(Integer vaultId, org.openapitools.model.CredentialDTO credentialDTO) {
        System.out.printf(credentialDTO.getTitle());
    }

}


