package org.backwarden.api.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CredentialAdapterTest {
    @Inject
    CredentialAdapter adapter;

    @Inject
    VaultAdapter vaultAdapter;

    @Inject
    UserAdapter userAdapter;

    //Test of hexagonal architecture (return value)
    @Test
    @Transactional
    void getCredential() {
        userAdapter.saveUser(new User());
        vaultAdapter.saveVault(1, new Vault());
        adapter.saveCredential(new Credential(), 1);
        Credential credential = adapter.getCredential(1);
        assertEquals(Credential.class, credential.getClass());
    }
}