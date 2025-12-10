package org.backwarden.api.adapters.controller.model.converter;

import org.backwarden.api.adapters.controller.model.CredentialDTO;
import org.backwarden.api.logic.model.Credential;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialDTOConverterTest
{
    @Test
    void testToDTO() {
        Credential credential = new Credential();
        credential.setId(1);
        credential.setTitle("Test Credential");
        credential.setPasswordSecure(true);
        credential.setUsername("testuser");
        credential.setPassword("testpass");
        credential.setPasswordCiphertext("encrypted");
        credential.setPasswordIV("iv");

        CredentialDTO dto = CredentialDTOConverter.toDTO(credential);

        assertEquals(credential.getId(), dto.getId());
        assertEquals(credential.getTitle(), dto.getTitle());
        assertEquals(credential.isPasswordSecure(), dto.isPasswordSecure());
        assertEquals(credential.getUsername(), dto.getUsername());
        assertEquals(credential.getPassword(), dto.getPassword());
        assertEquals(credential.getPasswordCiphertext(), dto.getPasswordCiphertext());
        assertEquals(credential.getPasswordIV(), dto.getPasswordIV());
    }

    @Test
    void testFromDTO() {
        CredentialDTO dto = new CredentialDTO();
        dto.setId(1);
        dto.setTitle("Test Credential");
        dto.setPasswordSecure(true);
        dto.setUsername("testuser");
        dto.setPassword("testpass");
        dto.setPasswordCiphertext("encrypted");
        dto.setPasswordIV("iv");

        Credential credential = CredentialDTOConverter.fromDTO(dto);

        assertEquals(dto.getId(), credential.getId());
        assertEquals(dto.getTitle(), credential.getTitle());
        assertEquals(dto.isPasswordSecure(), credential.isPasswordSecure());
        assertEquals(dto.getUsername(), credential.getUsername());
        assertEquals(dto.getPassword(), credential.getPassword());
        assertEquals(dto.getPasswordCiphertext(), credential.getPasswordCiphertext());
        assertEquals(dto.getPasswordIV(), credential.getPasswordIV());
        assertNull(credential.getVault());
    }

}