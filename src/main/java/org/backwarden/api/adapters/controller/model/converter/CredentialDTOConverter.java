package org.backwarden.api.adapters.controller.model.converter;


import org.backwarden.api.logic.model.Credential;
import org.openapitools.model.CredentialCreationDTO;
import org.openapitools.model.CredentialDTO;
import org.openapitools.model.CredentialUpdateDTO;


import java.util.List;

public class CredentialDTOConverter {

    public static CredentialDTO toDTO(Credential credential) {
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.setId(credential.getId());
        credentialDTO.setTitle(credential.getTitle());
        credentialDTO.setUsername(credential.getUsername());
        credentialDTO.setPassword(credential.getPassword());
        credentialDTO.setNote(credential.getNote());
        return credentialDTO;
    }

    public static Credential fromDTO(CredentialUpdateDTO credentialUpdateDTO) {
        Credential credential = new Credential();
        credential.setId(credentialUpdateDTO.getId());
        credential.setTitle(credentialUpdateDTO.getTitle());
        credential.setUsername(credentialUpdateDTO.getUsername());
        credential.setPassword(credentialUpdateDTO.getPassword());
        credential.setNote(credentialUpdateDTO.getNote());
        return credential;
    }

    public static Credential fromDTO(CredentialCreationDTO credentialCreationDTO) {
        Credential credential = new Credential();
        credential.setTitle(credentialCreationDTO.getTitle());
        credential.setUsername(credentialCreationDTO.getUsername());
        credential.setPassword(credentialCreationDTO.getPassword());
        return credential;
    }

    public static List<CredentialDTO> toDTOList(List<Credential> credentials) {
        return credentials.stream()
                .map(CredentialDTOConverter::toDTO)
                .toList();
    }
}
