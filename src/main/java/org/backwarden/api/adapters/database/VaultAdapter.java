package org.backwarden.api.adapters.database;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.VaultEntity;

@ApplicationScoped

public class VaultAdapter {
    @Inject
    EntityManager entityManager;

    @Transactional // Wichtig für Schreiboperationen
    public void saveVault(VaultEntity vault) {
        entityManager.persist(vault);
    }
    public VaultEntity getVault(long id) {
        return entityManager.find(VaultEntity.class, id);
    }
}
