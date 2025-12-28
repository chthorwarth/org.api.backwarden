package org.backwarden.api.adapters.database;
import jakarta.ws.rs.NotFoundException;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.adapters.database.model.converter.CredentialEntityConverter;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;

import java.util.List;

@ApplicationScoped

public class CredentialAdapter implements CredentialRepository {
    @Inject
    EntityManager entityManager;

    @Transactional
    @Override
    public void saveCredential(Credential credential)
    {
        entityManager.persist(CredentialEntityConverter.toEntity(credential));
    }

    public Credential getCredential(long id)
    {
        CredentialEntity credentialEntity = entityManager.find(CredentialEntity.class, id);

        return CredentialEntityConverter.fromEntity(credentialEntity);
    }


    //currently not working because of converter classes. Maybe we should add only vaultId to the DTO and model classes and save the Vault object in Entity class
    @Override
    public List<Credential> getAllCredentials(long vaultId)
    {
        VaultEntity vaultEntity = entityManager.find(VaultEntity.class, vaultId);

        if (vaultEntity == null)
        {
            throw new NotFoundException("Vault not found: " + vaultId);
        }

        List<CredentialEntity> credentialEntities = entityManager.createQuery("SELECT c FROM CredentialEntity c WHERE c.vault = :vaultEntity", CredentialEntity.class)
                .setParameter("vaultEntity",vaultEntity)
                .getResultList();
        git commit -m "added Credential functionality. We still have some issues about converter classes we have to discuss about."
        return CredentialEntityConverter.fromEntityList(credentialEntities);
    }

    @Override
    public void deleteCredential(long id)
    {
        CredentialEntity credentialEntity = entityManager.find(CredentialEntity.class, id);
        if (credentialEntity == null)
        {
            throw new NotFoundException("Credential with id " + id + " not found");
        }
        entityManager.remove(credentialEntity);
    }

    @Override
    public void updateCredential(long id, Credential credential)
    {
        CredentialEntity managedCredential = entityManager.find(CredentialEntity.class, id);
        if (managedCredential == null)
        {
            throw new NotFoundException("Vault with id " + id + " not found");
        }
        if (credential.getTitle() != null && !credential.getTitle().isEmpty())
            managedCredential.setTitle(credential.getTitle());
        if (credential.getUsername() != null && !credential.getUsername().isEmpty())
            managedCredential.setUsername(credential.getUsername());
        if (credential.getPasswordCiphertext() != null && !credential.getPasswordCiphertext().isEmpty())
            managedCredential.setPasswordCiphertext(credential.getPasswordCiphertext());
        if (credential.getPasswordIV() != null && !credential.getPasswordIV().isEmpty())
            managedCredential.setPasswordIV(credential.getPasswordIV());
        if (credential.getNote() != null && !credential.getNote().isEmpty())
            managedCredential.setNote(credential.getNote());
    }

}
