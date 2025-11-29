package org.backwarden.api.logic.services;

import org.backwarden.api.logic.ports.input.CredentialAPI;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;

public class CredentialService implements CredentialAPI
{
	CredentialRepository credentialRepository;

	public CredentialService(CredentialRepository credentialRepository) {
		this.credentialRepository = credentialRepository;
	}
    // hello

	@Override
	public void createCredentials(String username, String password) {
		credentialRepository.createAccount(0, "", 0);
	}
}
