package org.backwarden.api.logic.ports.input;

import org.backwarden.api.logic.model.Vault;

import java.util.List;

public interface VaultUseCase
{
    public void createVault(Vault vault);

    //public Vault getVault(long id);

    public void updateVault(long id, Vault vault);

    public void deleteVault(long id);

    public List<Vault> getAllVaults();

}
