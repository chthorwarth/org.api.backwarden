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
import org.backwarden.api.logic.exceptions.UserNotFoundException;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.output.persistence.UserRepository;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

@ApplicationScoped
public class UserAdapter implements UserRepository
{
    @Inject
    EntityManager entityManager;

    @Transactional
    @Override
    public User saveUser(User user)
    {
        UserEntity entity = UserEntityConverter.toEntity(user);
        try
        {
            entityManager.persist(entity);
            entityManager.flush();
        }
        catch (PersistenceException ex) {
            throw new EmailAlreadyExistsException("Email already exists in database");
        }
        return UserEntityConverter.fromEntity(entity);
    }

    public User getUser(long id) {
        UserEntity entity = entityManager.find(UserEntity.class, id);
        if (entity == null) {
            throw new UserNotFoundException("ID: " + id + " not found");
        }
        return UserEntityConverter.fromEntity(entity);
    }


}
