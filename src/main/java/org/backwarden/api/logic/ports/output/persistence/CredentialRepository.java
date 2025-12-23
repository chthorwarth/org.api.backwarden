package org.backwarden.api.logic.ports.output.persistence;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.logic.model.Credential;

public interface CredentialRepository
{
	public void saveCredential(Credential credential);

	public Credential getCredential(long id);

}
