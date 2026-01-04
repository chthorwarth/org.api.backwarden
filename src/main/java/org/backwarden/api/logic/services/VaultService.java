package org.backwarden.api.logic.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.input.VaultUseCase;
import org.backwarden.api.logic.ports.output.persistence.VaultRepository;

import java.util.List;
import java.util.NoSuchElementException;

@ApplicationScoped
public class VaultService implements VaultUseCase {
    @Inject
    VaultRepository vaultAdapter;

    @Override
    public long createVault(long userId, Vault vault) {
        Vault vault1 = vaultAdapter.saveVault(userId, vault);
        return vault1.getId();
    }

    /*@Override
    public Vault getVault(long id)
    {
        return vaultRepository.getVault(id);
    }*/

    @Override
    public void updateVault(long id, Vault vault) {
        vaultAdapter.updateVault(id, vault);
    }

    @Override
    public void deleteVault(long id) {
        vaultAdapter.deleteVault(id);
    }

    @Override
    public List<Vault> getAllVaults(long userId) {
        return vaultAdapter.getAllVaults(userId);
    }

    @Override
    public long getUserIdByVaultId(long vaultId) {
        return getVault(vaultId).getId();
    }

    @Override
    public Vault getVault(long vaultId) {
        Vault vault = vaultAdapter.getVault(vaultId);
        if (vault == null)
            throw new NoSuchElementException();
        return vault;
    }
}
