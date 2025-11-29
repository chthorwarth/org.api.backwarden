package logic.services;

import logic.ports.input.CredentialAPI;
import logic.ports.output.persistence.CredentialRepository;

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
