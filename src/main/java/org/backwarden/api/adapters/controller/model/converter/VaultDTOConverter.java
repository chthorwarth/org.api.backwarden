package org.backwarden.api.adapters.controller.model.converter;


import org.backwarden.api.logic.model.Vault;
import org.openapitools.model.VaultCreationDTO;
import org.openapitools.model.VaultDTO;
import org.openapitools.model.VaultUpdateDTO;

import java.util.List;

public class VaultDTOConverter {
    public static VaultDTO toDTO(Vault vault) {
        VaultDTO vaultDTO = new VaultDTO();
        vaultDTO.setId(vault.getId());
        vaultDTO.setTitle(vault.getTitle());
        vaultDTO.setAutoFill(vault.isAutoFill());
        return vaultDTO;
    }

    public static Vault fromDTO(VaultCreationDTO vaultCreationDTO) {
        Vault vault = new Vault();
        vault.setTitle(vaultCreationDTO.getTitle());
        vault.setAutoFill(vaultCreationDTO.getAutoFill());
        return vault;
    }

    public static Vault fromDTO(VaultUpdateDTO vaultUpdateDTO) {
        Vault vault = new Vault();
        vault.setId(vaultUpdateDTO.getId());
        vault.setTitle(vaultUpdateDTO.getTitle());
        vault.setAutoFill(vaultUpdateDTO.getAutoFill());
        return vault;
    }

    public static List<VaultDTO> toDTOList(List<Vault> vaults) {
        return vaults.stream()
                .map(VaultDTOConverter::toDTO)
                .toList();
    }
}
