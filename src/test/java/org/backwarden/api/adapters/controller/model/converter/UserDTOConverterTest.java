package org.backwarden.api.adapters.controller.model.converter;

import org.backwarden.api.adapters.controller.model.UserDTO;
import org.backwarden.api.adapters.controller.model.VaultDTO;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOConverterTest
{
    @Test
    void testToDTO() {
        User user = new User();
        user.setId(1);
        user.setMasterEmail("test@test.de");
        user.setFailedLoginAttempts(0);

        List<Vault> vaults = new ArrayList<>();
        Vault vault = new Vault();
        vault.setId(1);
        vault.setTitle("Test Vault");
        vaults.add(vault);
        user.setVaults(vaults);

        vault.setCredentials(new ArrayList<>());

        UserDTO dto = UserDTOConverter.toDTO(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getMasterEmail(), dto.getMasterEmail());
        assertEquals(user.getFailedLoginAttempts(), dto.getFailedLoginAttempts());
        assertEquals(user.getLockedUntil(), dto.getLockedUntil());
    }

    @Test
    void testFromDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(1);
        dto.setMasterEmail("test@test.de");
        dto.setFailedLoginAttempts(0);
        dto.setMasterPassword("testpass");

        List<VaultDTO> vaults = new ArrayList<>();
        VaultDTO vault = new VaultDTO();
        vault.setId(1);
        vault.setTitle("Test Vault");
        vault.setCredentials(new ArrayList<>());

        vaults.add(vault);


        User user = UserDTOConverter.fromDTO(dto);

        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getMasterEmail(), user.getMasterEmail());
        assertEquals(dto.getFailedLoginAttempts(), user.getFailedLoginAttempts());
        assertEquals(dto.getLockedUntil(), user.getLockedUntil());
        assertEquals(dto.getMasterPassword(), user.getMasterPassword());
    }

}