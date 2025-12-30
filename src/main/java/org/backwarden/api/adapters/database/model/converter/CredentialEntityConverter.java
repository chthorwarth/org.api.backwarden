package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.Vault;

import java.util.List;
import java.util.function.Function;

public class CredentialEntityConverter
{
    public static CredentialEntity toEntity(Credential credential)
    {
        CredentialEntity credentialEntity = new CredentialEntity();
        credentialEntity.setId(credential.getId());
        credentialEntity.setUsername(credential.getUsername());
        //credentialEntity.setPassword(credential.getPassword());
        credentialEntity.setNote(credential.getNote());
        credentialEntity.setTitle(credential.getTitle());
        credentialEntity.setPasswordCiphertext(credential.getPasswordCiphertext());
        //credentialEntity.setPasswordSecure(credential.isPasswordSecure());

        credentialEntity.setVault(null); //mapping results in endless loop
        return credentialEntity;
    }

    public static Credential fromEntity(CredentialEntity credentialEntity, Vault vault)
    {
        Credential credential = new Credential();

        credential.setId(credentialEntity.getId());
        credential.setUsername(credentialEntity.getUsername());
        //credential.setPassword(credentialEntity.getPassword());
        credential.setNote(credentialEntity.getNote());
        credential.setTitle(credentialEntity.getTitle());
        credential.setPasswordCiphertext(credentialEntity.getPasswordCiphertext());
        //credential.setPasswordSecure(credentialEntity.isPasswordSecure());

        credential.setVault(vault); //mapping results in endless loop

        return credential;
    }

    public static List<CredentialEntity> toEntityList(List<Credential> credentials)
    {
        return credentials.stream()
                .map(CredentialEntityConverter::toEntity)
                .toList();
    }

    public static List<Credential> fromEntityList(List<CredentialEntity> credentialEntities, Vault vault)
    {
        return credentialEntities.stream()
                .map(new Function<CredentialEntity, Credential>() {
                    @Override
                    public Credential apply(CredentialEntity credentialEntity)
                    {
                        return fromEntity(credentialEntity, vault);
                    }
                })
                .toList();
    }
}
