package org.backwarden.api.adapters.controller.model.converter;

import org.openapitools.model.UserDTO;
import org.backwarden.api.logic.model.User;
import org.openapitools.model.UserRegistrationDTO;

import java.util.Collections;

public class UserDTOConverter {
    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setMasterEmail(user.getMasterEmail());
        userDTO.setMasterPassword(user.getMasterPassword());
        userDTO.setFailedLoginAttempts(user.getFailedLoginAttempts());
        userDTO.setFailedLoginAttempts(user.getFailedLoginAttempts());
        return userDTO;
    }

    public static User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setMasterEmail(userDTO.getMasterEmail());
        user.setMasterPassword(userDTO.getMasterPassword());
        user.setFailedLoginAttempts(userDTO.getFailedLoginAttempts());
        return user;
    }

    public static User fromDTO(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setMasterEmail(userRegistrationDTO.getMasterEmail());
        user.setMasterPassword(userRegistrationDTO.getMasterPassword());
        return user;
    }
}
