package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;

import java.util.List;

public class UserEntityConverter {
    public static UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(user.getId());
        //userEntity.setVaults(VaultEntityConverter.toEntityList(user.getVaults()));
        userEntity.setFailedLoginAttempts(user.getFailedLoginAttempts());
        userEntity.setLockedUntil(user.getLockedUntil());
        userEntity.setMasterEmail(user.getMasterEmail());
        //userEntity.setMasterPassword(user.getMasterPassword());
        userEntity.setMasterPasswordHash(user.getMasterPasswordHash());
        userEntity.setMasterPasswordSalt(user.getMasterPasswordSalt());

        return userEntity;
    }

    public static User fromEntity(UserEntity userEntity) {
        User user = new User();

        user.setId(userEntity.getId());
        user.setFailedLoginAttempts(userEntity.getFailedLoginAttempts());
        user.setLockedUntil(userEntity.getLockedUntil());
        user.setMasterEmail(userEntity.getMasterEmail());
        //user.setMasterPassword(userEntity.getMasterPassword());
        user.setMasterPasswordHash(userEntity.getMasterPasswordHash());
        user.setMasterPasswordSalt(userEntity.getMasterPasswordSalt());


        user.setVaults(VaultEntityConverter.fromEntityList(userEntity.getVaults(), user));

        return user;
    }
/*
    public static List<UserEntity> toEntityList(List<User> users)
    {
        return users.stream()
                .map(UserEntityConverter::toEntity)
                .toList();
    }

    public static List<User> fromEntityList(List<UserEntity> userEntities)
    {
        return userEntities.stream()
                .map(UserEntityConverter::fromEntity)
                .toList();
    }
 */
}
