package org.backwarden.api.adapters.database;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;

@ApplicationScoped
public class DataInitializer
{
    @PersistenceContext
    EntityManager em;

    @PostConstruct
    @Transactional
    public void init()
    {
        UserEntity user = new UserEntity();
        user.setMasterEmail("max@example.com");
        user.setMasterPasswordHash("hash123");
        user.setMasterPasswordSalt("salt123");
        em.persist(user);

        VaultEntity personalVault = new VaultEntity();
        personalVault.setTitle("Persönliches Vault");
        personalVault.setAutoFill(true);
        user.addVault(personalVault);

        VaultEntity businessVault = new VaultEntity();
        businessVault.setTitle("Business Vault");
        businessVault.setAutoFill(false);
        user.addVault(businessVault);

        em.persist(personalVault);
        em.persist(businessVault);

        CredentialEntity emailCredential = new CredentialEntity();
        emailCredential.setTitle("Email");
        emailCredential.setUsername("max@example.com");
        emailCredential.setPasswordCiphertext("geheim123");
        personalVault.addCredential(emailCredential);

        CredentialEntity bankCredential = new CredentialEntity();
        bankCredential.setTitle("Bankkonto");
        bankCredential.setUsername("max_banker");
        bankCredential.setPasswordCiphertext("supersecret");
        personalVault.addCredential(bankCredential);

        CredentialEntity githubCredential = new CredentialEntity();
        githubCredential.setTitle("GitHub");
        githubCredential.setUsername("maxGH");
        githubCredential.setPasswordCiphertext("ghpass123");
        businessVault.addCredential(githubCredential);

        CredentialEntity awsCredential = new CredentialEntity();
        awsCredential.setTitle("AWS");
        awsCredential.setUsername("maxAWS");
        awsCredential.setPasswordCiphertext("awsSecret!");
        businessVault.addCredential(awsCredential);

        em.persist(emailCredential);
        em.persist(bankCredential);
        em.persist(githubCredential);
        em.persist(awsCredential);

        System.out.println("Testdaten für User, Vaults und Credentials wurden angelegt.");
    }
}
