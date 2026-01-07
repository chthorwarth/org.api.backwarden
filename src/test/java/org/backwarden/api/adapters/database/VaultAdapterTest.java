package org.backwarden.api.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class VaultAdapterTest
{
    @Inject
    VaultAdapter vaultAdapter;

    @Inject
    EntityManager entityManager;

    @Inject
    UserAdapter userAdapter;

    private User createTestUser(String email) {
        User user = new User();
        user.setMasterEmail(email);
        user.setMasterPasswordHash("hash");
        user.setMasterPasswordSalt("salt");
        return user;
    }

    private Vault createTestVault(String title, boolean autoFill) {
        Vault vault = new Vault();
        vault.setTitle(title);
        vault.setAutoFill(autoFill);
        return vault;
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
    void saveVault_shouldPersistVaultForExistingUser() {
        User user = createTestUser("vault@test.com");
        userAdapter.saveUser(user);
        entityManager.flush();
        entityManager.clear();

        long userId = entityManager
                .createQuery(
                        "SELECT u.id FROM UserEntity u WHERE u.masterEmail = :email",
                        Long.class)
                .setParameter("email", "vault@test.com")
                .getSingleResult();

        Vault vault = createTestVault("My Vault", true);

        vaultAdapter.saveVault(userId, vault);
        entityManager.flush();
        entityManager.clear();

        List<VaultEntity> vaults = entityManager
                .createQuery("SELECT v FROM VaultEntity v WHERE v.user.id = :userId", VaultEntity.class)
                .setParameter("userId", userId)
                .getResultList();

        assertEquals(1, vaults.size());
        assertEquals("My Vault", vaults.get(0).getTitle());
    }

    @Test
    @Transactional
    void saveVault_shouldFail_whenUserDoesNotExist() {
        Vault vault = createTestVault("Invalid", false);

        assertThrows(
                NotFoundException.class,
                () -> {
                    vaultAdapter.saveVault(99999L, vault);
                    entityManager.flush();
                }
        );
    }

    @Test
    @Transactional
    void updateVault_shouldUpdateExistingVault() {
        User user = createTestUser("update@test.com");
        userAdapter.saveUser(user);
        entityManager.flush();

        long userId = entityManager
                .createQuery("SELECT u.id FROM UserEntity u WHERE u.masterEmail = :email", Long.class)
                .setParameter("email", "update@test.com")
                .getSingleResult();

        Vault vault = createTestVault("Old Title", false);
        vaultAdapter.saveVault(userId, vault);
        entityManager.flush();
        entityManager.clear();


        long vaultId = entityManager
                .createQuery("SELECT v.id FROM VaultEntity v", Long.class)
                .getSingleResult();

        Vault updated = createTestVault("New Title", true);
        vaultAdapter.updateVault(vaultId, updated);
        entityManager.flush();
        entityManager.clear();

        VaultEntity reloaded = entityManager.find(VaultEntity.class, vaultId);

        Assertions.assertEquals("New Title", reloaded.getTitle());
        Assertions.assertTrue(reloaded.isAutoFill());
    }

    @Test
    void updateVault_shouldThrowNotFoundException_whenVaultDoesNotExist() {
        Vault vault = createTestVault("Does not matter", false);

        Assertions.assertThrows(
                NotFoundException.class,
                () -> vaultAdapter.updateVault(99999L, vault)
        );
    }


    @Test
    @Transactional
    void deleteVault_shouldRemoveExistingVault() {
        User user = createTestUser("delete@test.com");
        userAdapter.saveUser(user);
        entityManager.flush();

        long userId = entityManager
                .createQuery("SELECT u.id FROM UserEntity u WHERE u.masterEmail = :email", Long.class)
                .setParameter("email", "delete@test.com")
                .getSingleResult();

        Vault vault = createTestVault("Delete Me", false);
        vaultAdapter.saveVault(userId, vault);
        entityManager.flush();
        entityManager.clear();

        long vaultId = entityManager
                .createQuery("SELECT v.id FROM VaultEntity v", Long.class)
                .getSingleResult();

        vaultAdapter.deleteVault(vaultId);
        entityManager.flush();

        VaultEntity deleted = entityManager.find(VaultEntity.class, vaultId);
        Assertions.assertNull(deleted);
    }


    @Test
    void deleteVault_shouldThrowNotFoundException_whenVaultDoesNotExist() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> vaultAdapter.deleteVault(99999L)
        );
    }

    @Test
    @Transactional
    void getAllVaults_shouldReturnAllVaultsForUser() {
        User user = createTestUser("all@test.com");
        userAdapter.saveUser(user);
        entityManager.flush();

        long userId = entityManager
                .createQuery("SELECT u.id FROM UserEntity u WHERE u.masterEmail = :email", Long.class)
                .setParameter("email", "all@test.com")
                .getSingleResult();

        vaultAdapter.saveVault(userId, createTestVault("Vault 1", false));
        vaultAdapter.saveVault(userId, createTestVault("Vault 2", true));
        entityManager.flush();
        entityManager.clear();

        List<Vault> vaults = vaultAdapter.getAllVaults(userId);

        Assertions.assertEquals(2, vaults.size());
    }


    @Test
    @Transactional
    void getAllVaults_shouldBeEmpty_whenNoVaultsExist() {
        User user = createTestUser("empty@test.com");
        userAdapter.saveUser(user);
        entityManager.flush();

        long userId = entityManager
                .createQuery("SELECT u.id FROM UserEntity u WHERE u.masterEmail = :email", Long.class)
                .setParameter("email", "empty@test.com")
                .getSingleResult();

        Assertions.assertTrue(vaultAdapter.getAllVaults(userId).isEmpty());
    }



}