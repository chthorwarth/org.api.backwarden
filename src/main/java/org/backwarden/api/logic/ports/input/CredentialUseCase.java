package org.backwarden.api.logic.ports.input;

import org.backwarden.api.logic.model.Credential;

import java.util.List;


public interface CredentialUseCase {
    public long createCredentials(Credential credential, long vaultId);

    public Credential getCredential(long id);

    public List<Credential> getAllCredentials(long vaultId, String title);

    public void deleteCredential(long id);

    public void updateCredential(long id, Credential credential);
}
