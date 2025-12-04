package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.Vault;

import java.util.List;

public class VaultEntityConverter
{
    public static VaultEntity toEntity(Vault vault)
    {
        VaultEntity vaultEntity = new VaultEntity();
        vaultEntity.setId(vault.getId());
        vaultEntity.setTitle(vault.getTitle());
        vaultEntity.setCredentials(CredentialEntityConverter.toEntityList(vault.getCredentials()));

        vaultEntity.setUser(null); //mapping results in endless loop

        vaultEntity.setAutoFill(vault.isAutoFill());


        return vaultEntity;
    }

    public static Vault fromEntity(VaultEntity vaultEntity)
    {
        Vault vault = new Vault();
        vault.setId(vaultEntity.getId());
        vault.setTitle(vaultEntity.getTitle());
        vault.setCredentials(CredentialEntityConverter.fromEntityList(vaultEntity.getCredentials()));

        vault.setUser(null); //mapping results in endless loop

        vault.setAutoFill(vaultEntity.isAutoFill());

        return vault;
    }

    public static List<VaultEntity> toEntityList(List<Vault> vaults)
    {
        return vaults.stream()
                .map(VaultEntityConverter::toEntity)
                .toList();
    }

    public static List<Vault> fromEntityList(List<VaultEntity> vaultEntities)
    {
        return vaultEntities.stream()
                .map(VaultEntityConverter::fromEntity)
                .toList();
    }
}
