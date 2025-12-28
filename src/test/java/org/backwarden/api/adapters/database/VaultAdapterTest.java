package org.backwarden.api.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class VaultAdapterTest
{
    @Inject
    VaultAdapter adapter;

    //Test of hexagonal architecture (return value)
    @Test
    @Transactional
    void getVault()
    {

    }
}