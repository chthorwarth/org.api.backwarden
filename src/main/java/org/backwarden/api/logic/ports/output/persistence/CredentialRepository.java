package org.backwarden.api.logic.ports.output.persistence;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.logic.model.Credential;

import java.util.List;

public interface CredentialRepository
{
	public void saveCredential(Credential credential);

	public Credential getCredential(long id);

	public List<Credential> getAllCredentials(long vaultId);

	public void deleteCredential(long id);

	public void updateCredential(long id, Credential credential);
}
