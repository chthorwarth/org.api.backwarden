package org.backwarden.api.adapters.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.adapters.database.model.converter.UserEntityConverter;
import org.backwarden.api.adapters.database.model.converter.VaultEntityConverter;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.output.persistence.VaultRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped

public class VaultAdapter implements VaultRepository {
    @Inject
    EntityManager entityManager;

    @Transactional
    @Override
    public Vault saveVault(long userId, Vault vault) {
        UserEntity userEntity = entityManager.find(UserEntity.class, userId);
        VaultEntity vaultEntity = VaultEntityConverter.toEntity(vault);
        vaultEntity.setUser(userEntity);
        entityManager.persist(vaultEntity);
        User user = UserEntityConverter.fromEntity(vaultEntity.getUser());
        return VaultEntityConverter.fromEntity(vaultEntity, user);
    }

    @Override
    public Vault getVault(long id) {
        VaultEntity vaultEntity = entityManager.find(VaultEntity.class, id);
        User user = UserEntityConverter.fromEntity(vaultEntity.getUser());
        return VaultEntityConverter.fromEntity(vaultEntity, user);
    }

    @Transactional
    @Override
    public void updateVault(long id, Vault vault) {
        VaultEntity managedVault = entityManager.find(VaultEntity.class, id);
        if (managedVault == null) {
            throw new NotFoundException("Vault with id " + id + " not found");
        }

        //if (vault.getTitle() != null && !vault.getTitle().isEmpty())
        managedVault.setTitle(vault.getTitle());

        // We have to check for null, because the field is nullable in the database
        //if (vault.isAutoFill())
        managedVault.setAutoFill(vault.isAutoFill());
    }

    @Transactional
    @Override
    public void deleteVault(long id) {
        VaultEntity vaultEntity = entityManager.find(VaultEntity.class, id);
        if (vaultEntity == null) {
            throw new NotFoundException("Vault with id " + id + " not found");
        }
        entityManager.remove(vaultEntity);
    }

    @Override
    public List<Vault> getAllVaults(long userId) {
        List<VaultEntity> vaultEntities = entityManager.createQuery("SELECT v FROM VaultEntity v WHERE v.user.id = :userId", VaultEntity.class)
                .setParameter("userId", userId)
                .getResultList();
        if (vaultEntities.isEmpty()) {
            return new ArrayList<>();
        }
        User user = UserEntityConverter.fromEntity(vaultEntities.get(0).getUser());

        return VaultEntityConverter.fromEntityList(vaultEntities, user);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM VaultEntity").executeUpdate();
    }
}
