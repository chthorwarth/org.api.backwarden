package org.backwarden.api.adapters.controller;


import org.backwarden.api.adapters.database.CredentialRepository;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
@ApplicationScoped
public class CredentialController
{

    @Inject
    CredentialRepository credentialRepository;

	/*private final CredentialAPI credentialService;

	public CredentialController(CredentialAPI credentialService) {
		this.credentialService = credentialService;
	}

	public void createCredentials(String username, String password) {
		credentialService.createCredentials(username, password);
	}*/

	@GET
	public String hello() {
        System.out.println("Hello World");
        CredentialEntity credential = new CredentialEntity();
        credentialRepository.saveCredential(credential);
		return "Hello";
	}

}


