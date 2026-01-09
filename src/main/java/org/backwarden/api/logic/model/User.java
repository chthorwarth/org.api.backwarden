package org.backwarden.api.logic.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    /*
     *   We need to discuss again that if no vaults exist in a user, then vaults will be null.
     *   It might make sense to handle this error case earlier, specifically when instantiating a User object.
     */

    private long id;
    private String masterEmail;
    private String masterPassword;
    private String masterPasswordHash;
    private String masterPasswordSalt;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    private List<Vault> vaults = new ArrayList<>();

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

    public List<Vault> getVaults() {
        return vaults;
    }

    public void setVaults(List<Vault> vaults) {
        this.vaults = vaults;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && failedLoginAttempts == user.failedLoginAttempts && Objects.equals(masterEmail, user.masterEmail) && Objects.equals(masterPassword, user.masterPassword) && Objects.equals(masterPasswordHash, user.masterPasswordHash) && Objects.equals(masterPasswordSalt, user.masterPasswordSalt) && Objects.equals(lockedUntil, user.lockedUntil) && Objects.equals(vaults, user.vaults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, masterEmail, masterPassword, masterPasswordHash, masterPasswordSalt, failedLoginAttempts, lockedUntil);
    }
}
