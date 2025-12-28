package org.backwarden.api.adapters.database;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.adapters.database.model.converter.VaultEntityConverter;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.output.persistence.VaultRepository;

import java.util.List;

@ApplicationScoped

public class VaultAdapter implements VaultRepository
{
    @Inject
    EntityManager entityManager;

    @Transactional
    @Override
    public void saveVault(Vault vault)
    {
        entityManager.persist(VaultEntityConverter.toEntity(vault));
    }

    /*public Vault getVault(long id)
    {
        VaultEntity vaultEntity = entityManager.find(VaultEntity.class, id);
        return VaultEntityConverter.fromEntity(vaultEntity);
    }*/

    @Transactional
    @Override
    public void updateVault(long id, Vault vault)
    {
        VaultEntity managedVault = entityManager.find(VaultEntity.class, id);
        if (managedVault == null)
        {
            throw new NotFoundException("Vault with id " + id + " not found");
        }
        managedVault.setTitle(vault.getTitle());
        managedVault.setAutoFill(vault.isAutoFill());
    }

    @Transactional
    @Override
    public void deleteVault(long id)
    {
        VaultEntity vaultEntity = entityManager.find(VaultEntity.class, id);
        if (vaultEntity == null)
        {
            throw new NotFoundException("Vault with id " + id + " not found");
        }
        entityManager.remove(vaultEntity);
    }

    @Override
    public List<Vault> getAllVaults()
    {
        List<VaultEntity> vaultEntities = entityManager.createQuery("SELECT v FROM VaultEntity v", VaultEntity.class)
                .getResultList();

        return VaultEntityConverter.fromEntityList(vaultEntities);
    }
}
