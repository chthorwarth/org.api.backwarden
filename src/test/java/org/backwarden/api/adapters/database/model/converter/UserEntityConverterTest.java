package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityConverterTest {

    private User createTestUser() {
        User u = new User();
        u.setId(1L);
        u.setMasterEmail("test@example.com");
        u.setMasterPasswordHash("hash");
        u.setMasterPasswordSalt("salt");
        u.setFailedLoginAttempts(2);
        u.setLockedUntil(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.MIN));

        Vault vault1 = new Vault();
        vault1.setId(100L);
        vault1.setTitle("Vault1");
        Vault vault2 = new Vault();
        vault2.setId(101L);
        vault2.setTitle("Vault2");
        u.setVaults(List.of(vault1, vault2));

        return u;
    }

    @Test
    void toEntity() {
        User user = createTestUser();
        UserEntity entity = UserEntityConverter.toEntity(user);

        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getMasterEmail(), entity.getMasterEmail());
        assertEquals(user.getMasterPasswordHash(), entity.getMasterPasswordHash());
        assertEquals(user.getMasterPasswordSalt(), entity.getMasterPasswordSalt());
        assertEquals(user.getFailedLoginAttempts(), entity.getFailedLoginAttempts());
        assertEquals(user.getLockedUntil(), entity.getLockedUntil());

        assertTrue(entity.getVaults() == null || entity.getVaults().isEmpty());
    }

    @Test
    void fromEntity() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setMasterEmail("test@example.com");
        entity.setMasterPasswordHash("hash");
        entity.setMasterPasswordSalt("salt");
        entity.setFailedLoginAttempts(2);
        entity.setLockedUntil(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.MIN));

        Vault vault1 = new Vault();
        vault1.setId(100L);
        vault1.setTitle("Vault1");
        Vault vault2 = new Vault();
        vault2.setId(101L);
        vault2.setTitle("Vault2");
        for (Vault v : List.of(vault1, vault2))
            entity.addVault(VaultEntityConverter.toEntity(v));

        User user = UserEntityConverter.fromEntity(entity);

        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getMasterEmail(), user.getMasterEmail());
        assertEquals(entity.getMasterPasswordHash(), user.getMasterPasswordHash());
        assertEquals(entity.getMasterPasswordSalt(), user.getMasterPasswordSalt());
        assertEquals(entity.getFailedLoginAttempts(), user.getFailedLoginAttempts());
        assertEquals(entity.getLockedUntil(), user.getLockedUntil());

        assertNotNull(user.getVaults());
        assertEquals(2, user.getVaults().size());
        assertEquals("Vault1", user.getVaults().get(0).getTitle());
        assertEquals("Vault2", user.getVaults().get(1).getTitle());
    }
}
