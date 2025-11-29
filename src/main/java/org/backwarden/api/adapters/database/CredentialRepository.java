package org.backwarden.api.adapters.database;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped

public class CredentialRepository {
    @Inject
    EntityManager entityManager;

    @Transactional // Wichtig für Schreiboperationen
    public void saveCredential(CredentialEntity credential) {
        entityManager.persist(credential);
    }
}
