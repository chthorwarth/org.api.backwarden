package org.backwarden.api.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CredentialAdapterTest {

    @Inject
    CredentialAdapter credentialAdapter;

    @Inject
    UserAdapter userAdapter;

    @Inject
    VaultAdapter vaultAdapter;

    @Inject
    EntityManager entityManager;

    private User createTestUser(String email) {
        User user = new User();
        user.setMasterEmail(email);
        user.setMasterPasswordHash("hash");
        user.setMasterPasswordSalt("salt");
        return user;
    }

    private Vault createTestVault(String title) {
        Vault vault = new Vault();
        vault.setTitle(title);
        vault.setAutoFill(false);
        return vault;
    }

    private Credential createTestCredential(String title) {
        Credential credential = new Credential();
        credential.setTitle(title);
        credential.setUsername("user");
        credential.setPasswordCiphertext("cipher");
        credential.setPasswordIV("iv");
        credential.setNote("note");
        return credential;
    }

    @BeforeEach
    @Transactional
    void cleanup() {
        entityManager.createQuery("DELETE FROM CredentialEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM VaultEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();
    }

    @Test
    @Transactional
    void saveCredential_shouldPersistCredential()
    {
        User user = userAdapter.saveUser(createTestUser("cred@test.com"));
        entityManager.flush();

        vaultAdapter.saveVault(user.getId(), createTestVault("Vault"));
        entityManager.flush();
        entityManager.clear();

        long vaultId = entityManager
                .createQuery("SELECT v.id FROM VaultEntity v", Long.class)
                .getSingleResult();

        Credential credential = createTestCredential("Email Account");

        credentialAdapter.saveCredential(credential, vaultId);
        entityManager.flush();
        entityManager.clear();

        Long count = entityManager
                .createQuery("SELECT COUNT(c) FROM CredentialEntity c", Long.class)
                .getSingleResult();

        assertEquals(1L, count);
    }

    @Test
    @Transactional
    void saveCredential_shouldThrowNotFoundException_whenVaultDoesNotExist()
    {
        Credential credential = createTestCredential("Invalid");

        assertThrows(
                NotFoundException.class,
                () -> credentialAdapter.saveCredential(credential, 99999L)
        );
    }

    @Test
    @Transactional
    void getCredential_shouldReturnPersistedCredential()
    {
        User user = userAdapter.saveUser(createTestUser("get@test.com"));
        entityManager.flush();

        vaultAdapter.saveVault(user.getId(), createTestVault("Vault"));
        entityManager.flush();

        long vaultId = entityManager
                .createQuery("SELECT v.id FROM VaultEntity v", Long.class)
                .getSingleResult();

        credentialAdapter.saveCredential(createTestCredential("GitHub"), vaultId);
        entityManager.flush();
        entityManager.clear();

        long credentialId = entityManager
                .createQuery("SELECT c.id FROM CredentialEntity c", Long.class)
                .getSingleResult();

        Credential loaded = credentialAdapter.getCredential(credentialId);

        assertNotNull(loaded);
        assertEquals("GitHub", loaded.getTitle());
        assertNotNull(loaded.getVault());
    }

    @Test
    void getCredential_shouldThrowException_whenCredentialDoesNotExist()
    {
        assertThrows(
                NotFoundException.class,
                () -> credentialAdapter.getCredential(99999L)
        );
    }

    @Test
    @Transactional
    void getAllCredentials_shouldReturnAllCredentials_whenPageIsBigEnough() {
        User user = userAdapter.saveUser(createTestUser("all@test.com"));
        vaultAdapter.saveVault(user.getId(), createTestVault("Vault"));

        long vaultId = entityManager
                .createQuery("SELECT v.id FROM VaultEntity v", Long.class)
                .getSingleResult();

        credentialAdapter.saveCredential(createTestCredential("One"), vaultId);
        credentialAdapter.saveCredential(createTestCredential("Two"), vaultId);

        entityManager.flush();
        entityManager.clear();

        List<Credential> credentials = credentialAdapter.getAllCredentials(vaultId, 0, 10);

        assertEquals(2, credentials.size());
    }

    @Test
    @Transactional
    void getAllCredentials_shouldReturnPagedResults() {
        User user = userAdapter.saveUser(createTestUser("pagination@test.com"));
        vaultAdapter.saveVault(user.getId(), createTestVault("PagedVault"));
        long vaultId = entityManager.createQuery("SELECT v.id FROM VaultEntity v WHERE v.title = 'PagedVault'", Long.class).getSingleResult();

        for (int i = 1; i <= 5; i++) {
            credentialAdapter.saveCredential(createTestCredential("Cred-" + i), vaultId);
        }
        entityManager.flush();
        entityManager.clear();

        List<Credential> page0 = credentialAdapter.getAllCredentials(vaultId, 0, 2);
        assertEquals(2, page0.size(), "Page 0 should have 2 entries");

        List<Credential> page1 = credentialAdapter.getAllCredentials(vaultId, 1, 2);
        assertEquals(2, page1.size(), "Page 1 should have 2 entries");

        assertNotEquals(page0.get(0).getId(), page1.get(0).getId());

        List<Credential> page2 = credentialAdapter.getAllCredentials(vaultId, 2, 2);
        assertEquals(1, page2.size(), "Page 2 should have 1 entry");
    }

    @Test
    void getAllCredentials_shouldThrowNotFoundException_whenVaultDoesNotExist() {
        assertThrows(
                NotFoundException.class,
                () -> credentialAdapter.getAllCredentials(99999L, 0, 10)
        );
    }

    @Test
    @Transactional
    void countCredentials_shouldReturnCorrectTotalAmount() {
        User user = userAdapter.saveUser(createTestUser("count@test.com"));
        vaultAdapter.saveVault(user.getId(), createTestVault("CountVault"));
        long vaultId = entityManager.createQuery("SELECT v.id FROM VaultEntity v WHERE v.title = 'CountVault'", Long.class).getSingleResult();

        credentialAdapter.saveCredential(createTestCredential("A"), vaultId);
        credentialAdapter.saveCredential(createTestCredential("B"), vaultId);
        credentialAdapter.saveCredential(createTestCredential("C"), vaultId);
        entityManager.flush();

        long count = credentialAdapter.countCredentials(vaultId);

        assertEquals(3, count);
    }

    @Test
    @Transactional
    void deleteCredential_shouldRemoveCredential()
    {
        User user = userAdapter.saveUser(createTestUser("delete@test.com"));
        entityManager.flush();

        vaultAdapter.saveVault(user.getId(), createTestVault("Vault"));
        entityManager.flush();

        long vaultId = entityManager
                .createQuery("SELECT v.id FROM VaultEntity v", Long.class)
                .getSingleResult();

        credentialAdapter.saveCredential(createTestCredential("DeleteMe"), vaultId);
        entityManager.flush();

        long credentialId = entityManager
                .createQuery("SELECT c.id FROM CredentialEntity c", Long.class)
                .getSingleResult();

        credentialAdapter.deleteCredential(credentialId);
        entityManager.flush();

        assertNull(entityManager.find(
                org.backwarden.api.adapters.database.model.CredentialEntity.class,
                credentialId
        ));
    }

    @Test
    void deleteCredential_shouldThrowNotFoundException_whenCredentialDoesNotExist()
    {
        assertThrows(
                NotFoundException.class,
                () -> credentialAdapter.deleteCredential(99999L)
        );
    }

    @Test
    @Transactional
    void updateCredential_shouldUpdateFields()
    {
        User user = userAdapter.saveUser(createTestUser("update@test.com"));
        entityManager.flush();

        vaultAdapter.saveVault(user.getId(), createTestVault("Vault"));
        entityManager.flush();

        long vaultId = entityManager
                .createQuery("SELECT v.id FROM VaultEntity v", Long.class)
                .getSingleResult();

        credentialAdapter.saveCredential(createTestCredential("Old"), vaultId);
        entityManager.flush();

        long credentialId = entityManager
                .createQuery("SELECT c.id FROM CredentialEntity c", Long.class)
                .getSingleResult();

        Credential updated = new Credential();
        updated.setTitle("New");
        updated.setNote("Updated note");

        credentialAdapter.updateCredential(credentialId, updated);
        entityManager.flush();
        entityManager.clear();

        var entity = entityManager.find(
                org.backwarden.api.adapters.database.model.CredentialEntity.class,
                credentialId
        );

        assertEquals("New", entity.getTitle());
        assertEquals("Updated note", entity.getNote());
    }

    @Test
    void updateCredential_shouldThrowNotFoundException_whenCredentialDoesNotExist()
    {
        assertThrows(
                NotFoundException.class,
                () -> credentialAdapter.updateCredential(99999L, new Credential())
        );
    }
}
