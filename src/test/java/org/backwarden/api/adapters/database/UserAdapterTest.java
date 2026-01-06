package org.backwarden.api.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.logic.exceptions.EmailAlreadyExistsException;
import org.backwarden.api.logic.exceptions.UserNotFoundException;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserAdapterTest
{
    @Inject
    UserAdapter userAdapter;

    @Inject
    EntityManager entityManager;

    private User createTestUser(String email) {
        User user = new User();
        user.setMasterEmail(email);
        user.setMasterPasswordHash("hashed-password");
        user.setMasterPasswordSalt("salt");
        user.setFailedLoginAttempts(0);
        return user;
    }

    @BeforeEach
    @Transactional
    void cleanup()
    {
        entityManager.createQuery("DELETE FROM CredentialEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM VaultEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();
    }

    @Test
    @Transactional
    void saveUser_shouldPersistUserSuccessfully() {
        User user = createTestUser("test@example.com");
        User persisted = userAdapter.saveUser(user);
        entityManager.flush();
        entityManager.clear();

        assertTrue(persisted.getId() > 0);
    }

    @Test
    @Transactional
    void getUser_shouldReturnPersistedUser() {
        User user = createTestUser("load@example.com");
        User persisted = userAdapter.saveUser(user);
        entityManager.flush();
        entityManager.clear();

        User loadedUser = userAdapter.getUser(persisted.getId());

        Assertions.assertNotNull(loadedUser);
        Assertions.assertEquals(user.getMasterEmail(), loadedUser.getMasterEmail());
        Assertions.assertEquals(user.getMasterPasswordHash(), loadedUser.getMasterPasswordHash());
    }

    @Test
    @Transactional
    void saveUser_shouldThrowException_whenEmailAlreadyExists() {
        User first = createTestUser("duplicate@example.com");
        User second = createTestUser("duplicate@example.com");

        userAdapter.saveUser(first);
        entityManager.flush();

        Assertions.assertThrows(
                EmailAlreadyExistsException.class,
                () -> {
                    userAdapter.saveUser(second);
                    entityManager.flush();
                }
        );
    }

    @Test
    void getUser_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userAdapter.getUser(99999L)
        );
    }

    @Test
    void getUser_shouldThrowUserNotFoundException() {
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userAdapter.getUser(99999L)
        );
    }

    @Test
    @Transactional
    void getUser_shouldAlwaysHaveNonNullVaultList() {
        User user = createTestUser("vaults@example.com");
        User persisted = userAdapter.saveUser(user);
        entityManager.flush();
        entityManager.clear();

        User loaded = userAdapter.getUser(persisted.getId());

        Assertions.assertNotNull(loaded.getVaults(), "Vault-list cannot be null");
        Assertions.assertTrue(loaded.getVaults().isEmpty());
    }

}