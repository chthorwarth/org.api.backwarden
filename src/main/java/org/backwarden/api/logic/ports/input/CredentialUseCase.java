package org.backwarden.api.logic.ports.input;

import org.backwarden.api.logic.model.Credential;
import java.util.List;


public interface CredentialUseCase
{
	public void createCredentials(Credential credential);

	public Credential getCredential(int id);

	//public List<Credential> getAllCredentials(long vaultId);


}
