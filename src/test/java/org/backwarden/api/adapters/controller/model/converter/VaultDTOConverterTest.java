package org.backwarden.api.adapters.controller.model.converter;


import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.Vault;
import org.junit.jupiter.api.Test;
import org.openapitools.model.VaultDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VaultDTOConverterTest
{
    @Test
    void testToDTO() {
        Vault vault = new Vault();
        vault.setId(1);
        vault.setTitle("Test Vault");
        vault.setAutoFill(true);

        List<Credential> credentials = new ArrayList<>();
        Credential credential = new Credential();
        credential.setId(1);
        credential.setTitle("Test Credential");
        credentials.add(credential);
        vault.setCredentials(credentials);

        VaultDTO dto = VaultDTOConverter.toDTO(vault);

        assertEquals(vault.getId(), dto.getId());
        assertEquals(vault.getTitle(), dto.getTitle());
        assertEquals(vault.getCredentials().size(), dto.getCredentials().size());
        assertEquals(vault.getCredentials().get(0).getId(), dto.getCredentials().get(0).getId());
    }

    @Test
    void testFromDTO() {
        VaultDTO dto = new VaultDTO();
        dto.setTitle("Test Vault");
        dto.setAutoFill(true);
        dto.setCredentials(new ArrayList<>());

        Vault vault = VaultDTOConverter.fromDTO(dto);

        assertEquals(dto.getId(), vault.getId());
        assertEquals(dto.getTitle(), vault.getTitle());
        assertNull(vault.getUser()); // Prüfen, dass keine Endlosschleife entsteht
    }

    @Test
    void testToDTOList() {
        List<Vault> vaults = new ArrayList<>();
        Vault vault = new Vault();
        vault.setId(1);
        vault.setTitle("Test Vault");
        vaults.add(vault);
        vault.setCredentials(new ArrayList<>());

        List<VaultDTO> dtos = VaultDTOConverter.toDTOList(vaults);

        assertEquals(vaults.size(), dtos.size());
        assertEquals(vaults.get(0).getId(), dtos.get(0).getId());
        assertEquals(vaults.get(0).getTitle(), dtos.get(0).getTitle());
    }

    @Test
    void testFromDTOList() {
        List<VaultDTO> dtos = new ArrayList<>();
        VaultDTO dto = new VaultDTO();
        dto.setTitle("Test Vault");
        dtos.add(dto);
        dto.setCredentials(new ArrayList<>());

        List<Vault> vaults = VaultDTOConverter.fromDTOList(dtos);

        assertEquals(dtos.size(), vaults.size());
        assertEquals(dtos.get(0).getId(), vaults.get(0).getId());
        assertEquals(dtos.get(0).getTitle(), vaults.get(0).getTitle());
    }

}