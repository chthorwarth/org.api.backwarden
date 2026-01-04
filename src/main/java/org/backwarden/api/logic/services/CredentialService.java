package org.backwarden.api.logic.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.input.CredentialUseCase;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;

import java.util.List;


@ApplicationScoped
public class CredentialService implements CredentialUseCase {
    @Inject
    CredentialRepository credentialAdapter;

    @Override
    public long createCredentials(Credential credential, long vaultId) {
        Credential credential1 = credentialAdapter.saveCredential(credential, vaultId);
        return credential1.getId();
    }

    @Override
    public Credential getCredential(long id) {
        return credentialAdapter.getCredential(id);
    }

    @Override
    public List<Credential> getAllCredentials(long vaultId) {
        return credentialAdapter.getAllCredentials(vaultId);
    }

    @Override
    @Transactional
    public void deleteCredential(long id) {
        credentialAdapter.deleteCredential(id);
    }

    @Override
    public void updateCredential(long id, Credential credential) {
        credentialAdapter.updateCredential(id, credential);
    }
}
