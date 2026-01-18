package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CredentialEntityConverterTest {

    private Vault createTestVault() {
        Vault vault = new Vault();
        vault.setId(10L);
        vault.setTitle("Vault1");
        return vault;
    }

    private Credential createTestCredential() {
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("My Login");
        credential.setUsername("user");
        credential.setNote("Some note");
        credential.setPasswordCiphertext("cipher");
        credential.setPasswordIV("iv");
        credential.setVault(createTestVault());
        return credential;
    }

    @Test
    void toEntity() {
        Credential credential = createTestCredential();
        CredentialEntity entity = CredentialEntityConverter.toEntity(credential);

        assertEquals(credential.getId(), entity.getId());
        assertEquals(credential.getTitle(), entity.getTitle());
        assertEquals(credential.getUsername(), entity.getUsername());
        assertEquals(credential.getNote(), entity.getNote());
        assertEquals(credential.getPasswordCiphertext(), entity.getPasswordCiphertext());
        assertEquals(credential.getPasswordIV(), entity.getPasswordIV());

        // Vault sollte null sein (um Endlosschleife zu vermeiden)
        assertNull(entity.getVault());
    }

    @Test
    void fromEntity() {
        Vault vault = createTestVault();

        CredentialEntity entity = new CredentialEntity();
        entity.setId(1L);
        entity.setTitle("My Login");
        entity.setUsername("user");
        entity.setNote("Some note");
        entity.setPasswordCiphertext("cipher");
        entity.setPasswordIV("iv");

        Credential credential = CredentialEntityConverter.fromEntity(entity, vault);

        assertEquals(entity.getId(), credential.getId());
        assertEquals(entity.getTitle(), credential.getTitle());
        assertEquals(entity.getUsername(), credential.getUsername());
        assertEquals(entity.getNote(), credential.getNote());
        assertEquals(entity.getPasswordCiphertext(), credential.getPasswordCiphertext());
        assertEquals(entity.getPasswordIV(), credential.getPasswordIV());

        // Vault muss korrekt gesetzt sein
        assertNotNull(credential.getVault());
        assertEquals(vault.getId(), credential.getVault().getId());
    }

    @Test
    void toEntityList() {
        List<Credential> credentials = List.of(createTestCredential(), createTestCredential());
        List<CredentialEntity> entities = CredentialEntityConverter.toEntityList(credentials);

        assertEquals(credentials.size(), entities.size());
        assertEquals(credentials.get(0).getTitle(), entities.get(0).getTitle());
        assertNull(entities.get(0).getVault()); // Vault bleibt null
    }

    @Test
    void fromEntityList() {
        Vault vault = createTestVault();

        CredentialEntity entity1 = new CredentialEntity();
        entity1.setId(1L);
        entity1.setTitle("Login1");
        CredentialEntity entity2 = new CredentialEntity();
        entity2.setId(2L);
        entity2.setTitle("Login2");

        List<Credential> credentials = CredentialEntityConverter.fromEntityList(List.of(entity1, entity2), vault);

        assertEquals(2, credentials.size());
        assertEquals("Login1", credentials.get(0).getTitle());
        assertEquals("Login2", credentials.get(1).getTitle());

        // Vault muss korrekt gesetzt sein
        assertEquals(vault.getId(), credentials.get(0).getVault().getId());
        assertEquals(vault.getId(), credentials.get(1).getVault().getId());
    }
}
