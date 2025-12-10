package org.backwarden.api.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.logic.model.Credential;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CredentialAdapterTest
{
    @Inject
    CredentialAdapter adapter;

    @Test
    @Transactional
    void getCredential()
    {
        Credential credential = adapter.getCredential(1);
        assertEquals(Credential.class, credential.getClass());
    }
}