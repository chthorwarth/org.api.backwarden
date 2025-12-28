package org.backwarden.api.logic.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.input.VaultUseCase;
import org.backwarden.api.logic.ports.output.persistence.VaultRepository;

import java.util.List;

@ApplicationScoped
public class VaultService implements VaultUseCase
{
    @Inject
    VaultRepository vaultAdapter;

    @Override
    public void createVault(Vault vault)
    {
        vaultAdapter.saveVault(vault);
    }

    /*@Override
    public Vault getVault(long id)
    {
        return vaultRepository.getVault(id);
    }*/

    @Override
    public void updateVault(long id, Vault vault)
    {
        vaultAdapter.updateVault(id, vault);
    }

    @Override
    public void deleteVault(long id)
    {
        vaultAdapter.deleteVault(id);
    }

    @Override
    public List<Vault> getAllVaults()
    {
        return vaultAdapter.getAllVaults();
    }
}
