package org.backwarden.api.logic.ports.input;

import org.backwarden.api.logic.model.Vault;

import java.util.List;

public interface VaultUseCase {
    public long createVault(long userId, Vault vault);

    //public Vault getVault(long id);

    public void updateVault(long id, Vault vault);

    public void deleteVault(long id);

    public List<Vault> getAllVaults(long userId);

    public long getUserIdByVaultId(long vaultId);

    public Vault getVault(long vaultId);

}
