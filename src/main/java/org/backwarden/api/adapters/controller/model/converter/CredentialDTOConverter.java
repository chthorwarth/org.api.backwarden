package org.backwarden.api.adapters.controller.model.converter;

import org.backwarden.api.adapters.controller.model.CredentialDTO;
import org.backwarden.api.adapters.controller.model.VaultDTO;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.Vault;

import java.util.List;

public class CredentialDTOConverter {

    public static CredentialDTO toDTO(Credential credential) {
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.setId(credential.getId());
        credentialDTO.setTitle(credential.getTitle());
        credentialDTO.setPassword(credential.getPassword());
        credentialDTO.setUsername(credential.getUsername());
        credentialDTO.setPassword(credential.getPassword());
        credentialDTO.setPasswordCiphertext(credential.getPasswordCiphertext());
        credentialDTO.setPasswordIV(credential.getPasswordIV());
        credentialDTO.setNote(credential.getNote());
        return credentialDTO;
    }
    public static Credential fromDTO(CredentialDTO credentialDTO) {
        Credential credential = new Credential();
        credential.setId(credentialDTO.getId());
        credential.setVault(null);  //results in endless loop
        credential.setTitle(credentialDTO.getTitle());
        credential.setPasswordSecure(credentialDTO.isPasswordSecure());
        credential.setUsername(credentialDTO.getUsername());
        credential.setPassword(credentialDTO.getPassword());
        credential.setPasswordCiphertext(credentialDTO.getPasswordCiphertext());
        credential.setPasswordIV(credentialDTO.getPasswordIV());
        return credential;
    }

    public static List<CredentialDTO> toDTOList(List<Credential> credentials) {
        return credentials.stream()
                .map(CredentialDTOConverter::toDTO)
                .toList();
    }

    public static List<Credential> fromDTOList(List<CredentialDTO> credentialDTOs) {
        return credentialDTOs.stream()
                .map(CredentialDTOConverter::fromDTO)
                .toList();
    }
}
