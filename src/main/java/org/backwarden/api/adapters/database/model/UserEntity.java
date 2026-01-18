package org.backwarden.api.adapters.database.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class UserEntity {
    @Id
    // strategy identity: database is responsible for generating the id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String masterEmail;
    private String masterPasswordHash;
    private String masterPasswordSalt;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    // orphanRemoval: deletes a vault if it is removed from this user’s list
    // cascade all: changes on a child (vault) are automatically saved if parent (user) is saved
    // mappedBy: this side does not own the foreign key; it is stored in Vault.user
    // fetch lazy (by default): vaults are only loaded when accessed -> saves resources
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<VaultEntity> vaults = new ArrayList<>();


    /**
     * Adds a vault and keeps the bidirectional User–Vault relationship in sync.
     **/
    public void addVault(VaultEntity vault) {
        vaults.add(vault);
        vault.setUser(this);
    }

    /**
     * Removes a vault and keeps the bidirectional User-Vault relationship in sync
     **/
    public void removeVault(VaultEntity vault) {
        vaults.remove(vault);
        vault.setUser(null);
    }

    public List<VaultEntity> getVaults() {
        return vaults;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMasterEmail() {
        return masterEmail;
    }

    public void setMasterEmail(String masterEmail) {
        this.masterEmail = masterEmail;
    }

    public String getMasterPasswordHash() {
        return masterPasswordHash;
    }

    public void setMasterPasswordHash(String masterPasswordHash) {
        this.masterPasswordHash = masterPasswordHash;
    }

    public String getMasterPasswordSalt() {
        return masterPasswordSalt;
    }

    public void setMasterPasswordSalt(String masterPasswordSalt) {
        this.masterPasswordSalt = masterPasswordSalt;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(Instant lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}
