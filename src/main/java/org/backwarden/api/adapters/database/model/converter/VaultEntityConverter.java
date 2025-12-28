package org.backwarden.api.adapters.database.model.converter;

import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.model.Vault;

import java.util.List;
import java.util.function.Function;

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

    public static Vault fromEntity(VaultEntity vaultEntity, User user)
    {
        Vault vault = new Vault();
        vault.setId(vaultEntity.getId());
        vault.setTitle(vaultEntity.getTitle());
        vault.setCredentials(CredentialEntityConverter.fromEntityList(vaultEntity.getCredentials(), vault));

        vault.setUser(user); //mapping results in endless loop

        vault.setAutoFill(vaultEntity.isAutoFill());

        return vault;
    }

    public static List<VaultEntity> toEntityList(List<Vault> vaults)
    {
        return vaults.stream()
                .map(VaultEntityConverter::toEntity)
                .toList();
    }

    public static List<Vault> fromEntityList(List<VaultEntity> vaultEntities, User user)
    {
        return vaultEntities.stream()
                .map(new Function<VaultEntity, Vault>() {
                    @Override
                    public Vault apply(VaultEntity vaultEntity)
                    {
                        return fromEntity(vaultEntity, user);
                    }
                })
                .toList();
    }
}
