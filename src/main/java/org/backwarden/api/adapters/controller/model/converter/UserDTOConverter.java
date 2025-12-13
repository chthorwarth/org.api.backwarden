package org.backwarden.api.adapters.controller.model.converter;

import org.backwarden.api.adapters.controller.model.UserDTO;
import org.backwarden.api.logic.model.User;

import java.util.Collections;

public class UserDTOConverter {
    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setMasterEmail(user.getMasterEmail());
        userDTO.setMasterPassword(user.getMasterPassword());
        userDTO.setFailedLoginAttempts(user.getFailedLoginAttempts());
        userDTO.setFailedLoginAttempts(user.getFailedLoginAttempts());
        userDTO.setLockedUntil(user.getLockedUntil());
        return userDTO;
    }
    public static User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setMasterEmail(userDTO.getMasterEmail());
        user.setMasterPassword(userDTO.getMasterPassword());
        user.setFailedLoginAttempts(userDTO.getFailedLoginAttempts());
        user.setLockedUntil(userDTO.getLockedUntil());
        return user;
    }
}
