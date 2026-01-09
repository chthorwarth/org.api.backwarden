package org.backwarden.api.logic.model;

import java.util.Objects;

public class Credential {
    private long id;
    private Vault vault;
    private String title;
    private boolean isPasswordSecure;
    private String username;
    private String password;
    private String passwordCiphertext;
    private String passwordIV;
    private String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vault getVault() {
        return vault;
    }

    public void setVault(Vault vault) {
        this.vault = vault;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPasswordSecure() {
        return isPasswordSecure;
    }

    public void setPasswordSecure(boolean passwordSecure) {
        isPasswordSecure = passwordSecure;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordCiphertext() {
        return passwordCiphertext;
    }

    public void setPasswordCiphertext(String passwordCiphertext) {
        this.passwordCiphertext = passwordCiphertext;
    }

    public String getPasswordIV() {
        return passwordIV;
    }

    public void setPasswordIV(String passwordIV) {
        this.passwordIV = passwordIV;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Credential that = (Credential) o;
        return id == that.id && isPasswordSecure == that.isPasswordSecure && Objects.equals(vault, that.vault) && Objects.equals(title, that.title) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(passwordCiphertext, that.passwordCiphertext) && Objects.equals(passwordIV, that.passwordIV) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, isPasswordSecure, username, password, passwordCiphertext, passwordIV, note);
    }
}
