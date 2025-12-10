package org.backwarden.api.adapters.database;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.logic.model.User;

@ApplicationScoped

public class UserAdapter {
    @Inject
    EntityManager entityManager;

    @Transactional // Wichtig für Schreiboperationen
    public void saveUser(UserEntity user) {
        entityManager.persist(user);
    }

    public User getUser(long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        return new User(); // Muss hier schon in User gemappt werden?
    }
}
