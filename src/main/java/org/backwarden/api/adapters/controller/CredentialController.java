package org.backwarden.api.adapters.controller;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.backwarden.api.adapters.controller.model.CredentialDTO;
import org.backwarden.api.adapters.controller.model.interfaces.CredentialsApi;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.input.CredentialAPI;
import org.backwarden.api.logic.services.CredentialService;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CredentialController implements CredentialsApi
{

    CredentialAPI credentialAPI;

	@Override
    public List<CredentialDTO> vaultsVaultIdCredentialsGet(@PathParam("vaultId") Integer vaultId)
    {
        List<CredentialDTO> credentialDTOs = new ArrayList<>();
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.setId(1);
        credentialDTO.setTitle("Credential");
        credentialDTOs.add(credentialDTO);
        return credentialDTOs;
	}

    @Override
    public void vaultsVaultIdCredentialsPost(@PathParam("vaultId") Integer vaultId,@Valid @NotNull CredentialDTO credentialDTO)
    {
        System.out.printf(credentialDTO.getTitle());
    }

}


