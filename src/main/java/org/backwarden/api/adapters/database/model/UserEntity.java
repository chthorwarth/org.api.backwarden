package org.backwarden.api.adapters.database.model;

import java.time.Instant;
import java.util.List;


public class UserEntity {
    private long id;
    private String masterEmail;
    private String masterPassword;
    private String masterPasswordHash;
    private String masterPasswordSalt;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    private List<VaultEntity> vaults;

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

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
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

    public List<VaultEntity> getVaults() {
        return vaults;
    }

    public void setVaults(List<VaultEntity> vaults) {
        this.vaults = vaults;
    }
}
