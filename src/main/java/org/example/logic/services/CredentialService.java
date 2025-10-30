package org.example.logic.services;

import org.example.logic.ports.input.CredentialAPI;
import org.example.logic.ports.output.persistence.CredentialRepository;

public class CredentialService implements CredentialAPI
{
	CredentialRepository credentialRepository;

	public CredentialService(CredentialRepository credentialRepository) {
		this.credentialRepository = credentialRepository;
	}

	@Override
	public void createCredentials(String username, String password) {
		credentialRepository.createAccount(0, "", 0);
	}
}
