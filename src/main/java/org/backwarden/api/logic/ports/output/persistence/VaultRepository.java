package org.backwarden.api.logic.ports.output.persistence;


import org.backwarden.api.logic.model.Vault;

import java.util.List;

public interface VaultRepository {
    public Vault saveVault(long userId, Vault vault);

    //public Vault getVault(long id);

    public void updateVault(long id, Vault vault);

    public void deleteVault(long id);

    public List<Vault> getAllVaults(long userId);

    public Vault getVault(long vaultid);

    public void deleteAll();
}
