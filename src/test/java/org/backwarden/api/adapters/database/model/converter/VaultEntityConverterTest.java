package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VaultEntityConverterTest {

    private Credential createTestCredential(String title) {
        Credential c = new Credential();
        c.setId(1L);
        c.setTitle(title);
        c.setUsername("user");
        c.setPasswordCiphertext("cipher");
        c.setPasswordIV("iv");
        c.setNote("note");
        return c;
    }

    private Vault createTestVault() {
        Vault vault = new Vault();
        vault.setId(42L);
        vault.setTitle("My Vault");
        vault.setAutoFill(true);
        vault.setCredentials(List.of(createTestCredential("C1"), createTestCredential("C2")));
        return vault;
    }

    private User createTestUser() {
        User u = new User();
        u.setId(99L);
        u.setMasterEmail("test@example.com");
        return u;
    }

    @Test
    void toEntity() {
        Vault vault = createTestVault();
        VaultEntity entity = VaultEntityConverter.toEntity(vault);

        assertEquals(vault.getId(), entity.getId());
        assertEquals(vault.getTitle(), entity.getTitle());
        assertEquals(vault.isAutoFill(), entity.isAutoFill());
        assertNotNull(entity.getCredentials());
        assertEquals(vault.getCredentials().size(), entity.getCredentials().size());

        // Prüfe erste Credential
        CredentialEntity cEntity = entity.getCredentials().get(0);
        assertEquals("C1", cEntity.getTitle());
    }

    @Test
    void fromEntity() {
        Vault vault = createTestVault();
        User user = createTestUser();
        VaultEntity entity = VaultEntityConverter.toEntity(vault);

        Vault converted = VaultEntityConverter.fromEntity(entity, user);

        assertEquals(entity.getId(), converted.getId());
        assertEquals(entity.getTitle(), converted.getTitle());
        assertEquals(entity.isAutoFill(), converted.isAutoFill());
        assertEquals(user, converted.getUser());
        assertEquals(entity.getCredentials().size(), converted.getCredentials().size());

        // Prüfe erste Credential
        assertEquals("C1", converted.getCredentials().get(0).getTitle());
    }

    @Test
    void toEntityList() {
        List<Vault> vaults = List.of(createTestVault(), createTestVault());
        List<VaultEntity> entities = VaultEntityConverter.toEntityList(vaults);

        assertEquals(vaults.size(), entities.size());
        assertEquals(vaults.get(0).getId(), entities.get(0).getId());
    }

    @Test
    void fromEntityList() {
        User user = createTestUser();
        List<Vault> vaults = List.of(createTestVault(), createTestVault());
        List<VaultEntity> entities = VaultEntityConverter.toEntityList(vaults);

        List<Vault> converted = VaultEntityConverter.fromEntityList(entities, user);

        assertEquals(entities.size(), converted.size());
        assertEquals(user, converted.get(0).getUser());
        assertEquals("My Vault", converted.get(0).getTitle());
    }
}
