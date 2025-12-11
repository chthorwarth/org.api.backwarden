package org.backwarden.api.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit .jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import static org.junit.jupiter.api.Assertions.assertEquals;


/** TODO
 * following tests are missing:
 * - cascade is working as expected
 * - orphan removal is working as expected
 * - is bidirectionality working
 * - lazy loading is working as expected
 */
@QuarkusTest
public class TestDB

{

    @Inject
    EntityManager em;
    UserEntity user = new UserEntity();
    @Transactional
    void init()
    {
        List<CredentialEntity> credentials = new ArrayList<CredentialEntity>();
        credentials.add(new CredentialEntity());

        VaultEntity vault = new VaultEntity();

        vault.setTitle("Vault");
        vault.setCredentials(credentials);
        vault.setAutoFill(true);


        user.setMasterEmail("simon@thws");
        user.setMasterPasswordHash("Password");
        user.setMasterPasswordHash("PasswordHash");
        user.setMasterPasswordSalt("PasswordSalt");
        user.setFailedLoginAttempts(1);
        user.setLockedUntil(null);
        user.addVault(vault);

        vault.setUser(user);

        CredentialEntity cred = new CredentialEntity();

        cred.setVault(vault);
        cred.setTitle("Credential");
        cred.setUsername("Username");
        cred.setPasswordCiphertext("PasswordCiphertext");
        cred.setPasswordIV("PasswordIV");
        cred.setNote("Note");

        em.persist(user);

    }
    @Test
    @Transactional
    void testDbInit()
    {
        init();
        UserEntity loaded = em.find(UserEntity.class, user.getId());
        assertEquals(user.getId(),  loaded.getId());
    }
}
