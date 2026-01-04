package org.backwarden.api.adapters.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.adapters.database.model.converter.CredentialEntityConverter;
import org.backwarden.api.adapters.database.model.converter.UserEntityConverter;
import org.backwarden.api.adapters.database.model.converter.VaultEntityConverter;
import org.backwarden.api.logic.exceptions.EmailAlreadyExistsException;
import org.backwarden.api.logic.exceptions.UserDoesNotExistException;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.output.persistence.UserRepository;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

@ApplicationScoped
public class UserAdapter implements UserRepository {
    @Inject
    EntityManager entityManager;

    @Transactional
    @Override
    public long saveUser(User user) {

        try {
            UserEntity userEntity = UserEntityConverter.toEntity(user);
            entityManager.persist(userEntity);
            entityManager.flush();
            return userEntity.getId();
        } catch (PersistenceException ex) {
            throw new EmailAlreadyExistsException("Email already exists in database"); // eigene Domain-Exception
        }
    }

    public User getUser(long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);

        return UserEntityConverter.fromEntity(userEntity);
    }

    @Override
    public User getUser(String mail) {
        UserEntity entity = entityManager
                .createQuery("SELECT u FROM UserEntity u WHERE u.masterEmail = :mail", UserEntity.class)
                .setParameter("mail", mail).getSingleResultOrNull();
        if (entity == null)
            throw new UserDoesNotExistException("Can't find user with this mail");
        return UserEntityConverter.fromEntity(entity);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();
    }


}
