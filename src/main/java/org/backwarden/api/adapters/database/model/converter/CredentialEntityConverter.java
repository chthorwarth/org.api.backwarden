package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.logic.model.Credential;

import java.util.List;

public class CredentialEntityConverter
{
    public static CredentialEntity toEntity(Credential credential)
    {
        CredentialEntity credentialEntity = new CredentialEntity();
        credentialEntity.setId(credential.getId());
        credentialEntity.setUsername(credential.getUsername());
        credentialEntity.setNote(credential.getNote());
        credentialEntity.setTitle(credential.getTitle());
        credentialEntity.setPasswordCiphertext(credential.getPasswordCiphertext());

        credentialEntity.setVault(null); //mapping results in endless loop
        return credentialEntity;
    }

    public static Credential fromEntity(CredentialEntity credentialEntity)
    {
        Credential credential = new Credential();

        credential.setId(credentialEntity.getId());
        credential.setUsername(credentialEntity.getUsername());
        credential.setNote(credentialEntity.getNote());
        credential.setTitle(credentialEntity.getTitle());
        credential.setPasswordCiphertext(credentialEntity.getPasswordCiphertext());

        credential.setVault(null); //mapping results in endless loop

        return credential;
    }

    public static List<CredentialEntity> toEntityList(List<Credential> credentials)
    {
        return credentials.stream()
                .map(CredentialEntityConverter::toEntity)
                .toList();
    }

    public static List<Credential> fromEntityList(List<CredentialEntity> credentialEntities)
    {
        return credentialEntities.stream()
                .map(CredentialEntityConverter::fromEntity)
                .toList();
    }
}
