package org.backwarden.api.adapters.database;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.NotFoundException;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.adapters.database.model.converter.CredentialEntityConverter;
import org.backwarden.api.adapters.database.model.converter.UserEntityConverter;
import org.backwarden.api.adapters.database.model.converter.VaultEntityConverter;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;

import java.util.List;

@ApplicationScoped

public class CredentialAdapter implements CredentialRepository {
    @Inject
    EntityManager entityManager;

    @Transactional
    @Override
    public Credential saveCredential(Credential credential, long vaultId) {
        VaultEntity vaultEntity = entityManager.find(VaultEntity.class, vaultId);
        if (vaultEntity == null) {
            throw new NotFoundException("Vault not found: " + vaultId);
        }
        CredentialEntity credentialEntity = CredentialEntityConverter.toEntity(credential);
        credentialEntity.setVault(vaultEntity);
        User user = UserEntityConverter.fromEntity(credentialEntity.getVault().getUser());
        Vault vault = VaultEntityConverter.fromEntity(credentialEntity.getVault(), user);

        entityManager.persist(credentialEntity);
        return CredentialEntityConverter.fromEntity(credentialEntity, VaultEntityConverter.fromEntity(vaultEntity, UserEntityConverter.fromEntity(vaultEntity.getUser())));
    }

    @Override
    public Credential getCredential(long id) {
        CredentialEntity credentialEntity = entityManager.find(CredentialEntity.class, id);
        if (credentialEntity == null)
            throw new NotFoundException("Credential not found: " + id);
        User user = UserEntityConverter.fromEntity(credentialEntity.getVault().getUser());
        Vault vault = VaultEntityConverter.fromEntity(credentialEntity.getVault(), user);

        return CredentialEntityConverter.fromEntity(credentialEntity, vault);
    }


    @Override
    public List<Credential> getAllCredentials(long vaultId, String title, int page, int size) {

        VaultEntity vaultEntity = entityManager.find(VaultEntity.class, vaultId);

        if (vaultEntity == null) {
            throw new NotFoundException("Vault not found: " + vaultId);
        }



        String sql =
                "SELECT c FROM CredentialEntity c " +
                        "WHERE c.vault.id = :vaultId";

        if (title != null && !title.isBlank()) {
            sql += " AND LOWER(c.title) LIKE :title";
        }

        TypedQuery<CredentialEntity> filter =
                entityManager.createQuery(sql, CredentialEntity.class)
                        .setParameter("vaultId", vaultEntity.getId());

        if (title != null && !title.isBlank()) {
            filter.setParameter("title", "%" + title.toLowerCase() + "%");
        }

        List<CredentialEntity> credentialEntities = filter.setFirstResult(page * size)
                                                             .setMaxResults(size)
                                                               .getResultList();

        User user = UserEntityConverter.fromEntity(vaultEntity.getUser());

        return CredentialEntityConverter.fromEntityList(
                credentialEntities,
                VaultEntityConverter.fromEntity(vaultEntity, user)
        );
    }


    @Override
    public void deleteCredential(long id) {
        CredentialEntity credentialEntity = entityManager.find(CredentialEntity.class, id);
        if (credentialEntity == null) {
            throw new NotFoundException("Credential with id " + id + " not found");
        }
        entityManager.remove(credentialEntity);
    }

    @Override
    public void updateCredential(long id, Credential credential) {
        CredentialEntity managedCredential = entityManager.find(CredentialEntity.class, id);
        if (managedCredential == null) {
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

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM CredentialEntity").executeUpdate();
    }

    @Override
    public long countCredentials(long vaultId, String title) {
        String sql =
                "SELECT COUNT(c) FROM CredentialEntity c WHERE c.vault.id = :vaultId";
        if (title != null && !title.isBlank()) {
            sql += " AND LOWER(c.title) LIKE :title";
        }
        TypedQuery<Long> query = entityManager.createQuery(
                        sql, Long.class)
                .setParameter("vaultId", vaultId);

        if (title != null && !title.isBlank()) {
            query.setParameter("title", "%" + title.toLowerCase() + "%");
        }
        return query.getSingleResult();

    }

}
