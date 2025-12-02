package org.backwarden.api.adapters.controller;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.backwarden.api.adapters.controller.model.CredentialDTO;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.input.CredentialAPI;
import org.backwarden.api.logic.services.CredentialService;

import java.util.ArrayList;
import java.util.List;

@Path("/credential")
@ApplicationScoped
public class CredentialController
{

    CredentialAPI credentialAPI;

	@GET
	public List<CredentialDTO> getCredentials() {
        credentialAPI.getCredential(5);
        return new ArrayList<CredentialDTO>();
	}

    @POST
    public void createCredentials() {
        credentialAPI.createCredentials(new Credential());
    }

}


