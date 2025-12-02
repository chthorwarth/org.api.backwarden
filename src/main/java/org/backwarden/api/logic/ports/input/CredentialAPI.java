package org.backwarden.api.logic.ports.input;

import org.backwarden.api.logic.model.Credential;

public interface CredentialAPI
{
	public void createCredentials(Credential credential);

	public Credential getCredential(int id);
}
