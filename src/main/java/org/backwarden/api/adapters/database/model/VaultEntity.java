package org.backwarden.api.adapters.database.model;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class VaultEntity {
    @Id
    // strategy identity: database is responsible for generating the id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    // manyToOne: creates a foreign key column (user_id) in this table pointing to the User's id
    // fetch lazy: user is only loaded when accessed -> saves resources
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    // orphanRemoval: deletes a credential if it is removed from this vault’s list
    // cascade all: changes on a child (credential) are automatically saved if parent (vault) is saved
    // mappedBy: this side does not own the foreign key; it is stored in CredentialEntity.vault
    // fetch lazy (by default): credentials are only loaded when accessed -> saves resources
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vault")
    private List<CredentialEntity> credentials = new ArrayList<>();
    private boolean autoFill;

    /** Adds a credential and keeps the bidirectional Vault–Credential relationship in sync. **/
    // TODO write a test for this
    public void addCredential(CredentialEntity credential) {
        credentials.add(credential);
        credential.setVault(this);
    }

    /** Removes a credential and keeps the bidirectional Vault-Credential relationship in sync **/
    // TODO write a test for this
    public void removeCredential(CredentialEntity credential) {
        credentials.remove(credential);
        credential.setVault(null);
    }

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<CredentialEntity> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialEntity> credentials) {
        this.credentials = credentials;
    }

    public boolean isAutoFill() {
        return autoFill;
    }

    public void setAutoFill(boolean autoFill) {
        this.autoFill = autoFill;
    }
}
