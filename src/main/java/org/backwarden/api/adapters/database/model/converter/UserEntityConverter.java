package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.logic.model.User;

public class UserEntityConverter
{
    public static UserEntity toEntity(User user)
    {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(user.getId());
        userEntity.setFailedLoginAttempts(user.getFailedLoginAttempts());
        userEntity.setLockedUntil(user.getLockedUntil());
        userEntity.setMasterEmail(user.getMasterEmail());
        userEntity.setMasterPasswordHash(user.getMasterPasswordHash());
        userEntity.setMasterPasswordSalt(user.getMasterPasswordSalt());

        return userEntity;
    }

    public static User fromEntity(UserEntity userEntity)
    {
        User user = new User();

        user.setId(userEntity.getId());
        user.setFailedLoginAttempts(userEntity.getFailedLoginAttempts());
        user.setLockedUntil(userEntity.getLockedUntil());
        user.setMasterEmail(userEntity.getMasterEmail());
        user.setMasterPasswordHash(userEntity.getMasterPasswordHash());
        user.setMasterPasswordSalt(userEntity.getMasterPasswordSalt());

        return user;
    }
}
