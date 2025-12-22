package org.backwarden.api.logic.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.input.CredentialUseCase;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;


@ApplicationScoped
public class CredentialService implements CredentialUseCase
{
	CredentialRepository credentialRepository;

	@Override
	public void createCredentials(Credential credential) {
		credentialRepository.saveCredential(new Credential());
	}

	//TODO change id into long!
	@Override
	public Credential getCredential(int id) {
		return credentialRepository.getCredential(id);
	}
}
