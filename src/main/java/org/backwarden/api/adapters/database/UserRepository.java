package org.backwarden.api.adapters.database;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.UserEntity;

@ApplicationScoped

public class UserRepository {
    @Inject
    EntityManager entityManager;

    @Transactional // Wichtig für Schreiboperationen
    public void saveUser(UserEntity user) {
        entityManager.persist(user);
    }
    public UserEntity getUser(long id) {
        return entityManager.find(UserEntity.class, id);
    }
}
