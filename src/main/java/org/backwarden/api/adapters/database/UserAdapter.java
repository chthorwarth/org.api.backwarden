package org.backwarden.api.adapters.database;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.adapters.database.model.converter.CredentialEntityConverter;
import org.backwarden.api.adapters.database.model.converter.UserEntityConverter;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.ports.output.persistence.UserRepository;

import java.util.List;

@ApplicationScoped
public class UserAdapter implements UserRepository
{
    @Inject
    EntityManager entityManager;

    @Transactional
    @Override
    public void saveUser(User user)
    {
        entityManager.persist(UserEntityConverter.toEntity(user));
    }

    public User getUser(long id)
    {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        return UserEntityConverter.fromEntity(userEntity);
    }

}
