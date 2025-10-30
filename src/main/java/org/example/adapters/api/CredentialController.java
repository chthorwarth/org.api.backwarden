package org.example.adapters.api;

import org.example.logic.ports.input.CredentialAPI;

public class CredentialController
{

	private final CredentialAPI credentialService;

	public CredentialController(CredentialAPI credentialService) {
		this.credentialService = credentialService;
	}

	public void createCredentials(String username, String password) {
		credentialService.createCredentials(username, password);
	}
}
