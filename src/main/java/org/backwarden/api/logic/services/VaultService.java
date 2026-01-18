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
    VaultRepository vaultRepository;

    @Override
    public long createVault(long userId, Vault vault) {
        Vault vault1 = vaultRepository.saveVault(userId, vault);
        return vault1.getId();
    }

    @Override
    public void updateVault(long id, Vault vault) {
        vaultRepository.updateVault(id, vault);
    }

    @Override
    public void deleteVault(long id) {
        vaultRepository.deleteVault(id);
    }

    @Override
    public List<Vault> getAllVaults(long userId) {
        return vaultRepository.getAllVaults(userId);
    }

    @Override
    public long getUserIdByVaultId(long vaultId) {
        return getVault(vaultId).getUser().getId();
    }

    @Override
    public Vault getVault(long vaultId) {
        Vault vault = vaultRepository.getVault(vaultId);
        if (vault == null)
            throw new NoSuchElementException();
        return vault;
    }
}
