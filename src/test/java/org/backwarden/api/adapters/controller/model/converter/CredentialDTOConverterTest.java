package org.backwarden.api.adapters.controller.model.converter;


import org.backwarden.api.logic.model.Credential;
import org.junit.jupiter.api.Test;
import org.openapitools.model.CredentialCreationDTO;
import org.openapitools.model.CredentialDTO;
import org.openapitools.model.CredentialUpdateDTO;

import static org.junit.jupiter.api.Assertions.*;

class CredentialDTOConverterTest {
    @Test
    void testToDTO() {
        Credential credential = new Credential();
        credential.setId(1);
        credential.setTitle("Test Credential");
        credential.setPasswordSecure(true);
        credential.setUsername("testuser");
        credential.setPasswordCiphertext("encrypted");
        credential.setPasswordIV("iv");

        CredentialDTO dto = CredentialDTOConverter.toDTO(credential);

        assertEquals(credential.getId(), dto.getId());
        assertEquals(credential.getTitle(), dto.getTitle());
        assertEquals(credential.getUsername(), dto.getUsername());
        assertEquals(credential.getPasswordCiphertext(), dto.getPasswordCiphertext());
        assertEquals(credential.getPasswordIV(), dto.getPasswordIV());
    }

    @Test
    void testFromCredentialCreationDTO() {
        CredentialCreationDTO dto = new CredentialCreationDTO();

        dto.setTitle("Test Credential");

        dto.setUsername("testuser");
        dto.setPassword("testpass");

        Credential credential = CredentialDTOConverter.fromDTO(dto);

        assertEquals(dto.getTitle(), credential.getTitle());
        assertEquals(dto.getUsername(), credential.getUsername());
        assertEquals(dto.getPassword(), credential.getPassword());
        assertNull(credential.getVault());
    }

    @Test
    void testFromCredentialUpdateDTO() {
        CredentialUpdateDTO dto = new CredentialUpdateDTO();

        dto.setId(1L);
        dto.setTitle("Test Credential");

        dto.setUsername("testuser");
        dto.setPassword("testpass");

        Credential credential = CredentialDTOConverter.fromDTO(dto);

        assertEquals(dto.getId(), credential.getId());
        assertEquals(dto.getTitle(), credential.getTitle());
        assertEquals(dto.getUsername(), credential.getUsername());
        assertEquals(dto.getPassword(), credential.getPassword());
        assertNull(credential.getVault());
    }

}