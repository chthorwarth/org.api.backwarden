package org.backwarden.api.logic.services;

import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.input.CredentialAPI;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;

public class CredentialService implements CredentialAPI
{
	CredentialRepository credentialRepository;

	@Override
	public void createCredentials(Credential credential) {
		credentialRepository.saveCredential(new Credential());
	}

	@Override
	public Credential getCredential(int id) {
		return credentialRepository.getCredential(id);
	}
}
