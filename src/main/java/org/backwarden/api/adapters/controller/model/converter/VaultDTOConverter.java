package org.backwarden.api.adapters.controller.model.converter;


import org.backwarden.api.logic.model.Vault;
import org.openapitools.model.VaultDTO;

import java.util.List;

public class VaultDTOConverter {
    public static VaultDTO toDTO(Vault vault) {
        VaultDTO vaultDTO = new VaultDTO();
        vaultDTO.setId(vault.getId());
        vaultDTO.setTitle(vault.getTitle());
        vaultDTO.setCredentials(CredentialDTOConverter.toDTOList(vault.getCredentials()));
        vaultDTO.setAutoFill(vault.isAutoFill());
        return vaultDTO;
    }
    public static Vault fromDTO(VaultDTO vaultDTO) {
        Vault vault = new Vault();
        vault.setTitle(vaultDTO.getTitle());
        vault.setUser(null);        //results in endless loop
        vault.setCredentials(CredentialDTOConverter.fromDTOList(vaultDTO.getCredentials()));
        return vault;
    }
    public static List<VaultDTO> toDTOList(List<Vault> vaults) {
        return vaults.stream()
                .map(VaultDTOConverter::toDTO)
                .toList();
    }

    public static List<Vault> fromDTOList(List<VaultDTO> vaultDTOs) {
        return vaultDTOs.stream()
                .map(VaultDTOConverter::fromDTO)
                .toList();
    }
}
