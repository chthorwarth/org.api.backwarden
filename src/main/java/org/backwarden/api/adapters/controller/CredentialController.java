package org.backwarden.api.adapters.controller;


import org.backwarden.api.adapters.database.CredentialRepository;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.backwarden.api.logic.model.Credential;

@Path("/credential")
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
        credential.setPasswordCiphertext("password");
        credentialRepository.saveCredential(credential);

        CredentialEntity credentialNew = credentialRepository.getCredential(1);
        System.out.println(credentialNew.getPasswordCiphertext());
		return credentialNew.getPasswordCiphertext();

	}

}


