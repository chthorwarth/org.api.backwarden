package org.backwarden.api.adapters.database.model;

import jakarta.persistence.*;

@Entity
public class CredentialEntity {
    @Id
    // strategy identity: database is responsible for generating the id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // manyToOne: creates a foreign key column (vault_id) in this table pointing to the Vault's id
    @ManyToOne(fetch = FetchType.LAZY)
    private VaultEntity vault;
    private String title;
    private String username;
    private String passwordCiphertext;
    private String passwordIV;
    private String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public VaultEntity getVault() {
        return vault;
    }

    public void setVault(VaultEntity vault) {
        this.vault = vault;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
