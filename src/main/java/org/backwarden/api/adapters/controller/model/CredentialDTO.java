package org.backwarden.api.adapters.controller.model;

import org.backwarden.api.logic.model.Vault;

public class CredentialDTO {
    private long id;
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
}
