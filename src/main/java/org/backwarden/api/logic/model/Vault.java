package org.backwarden.api.logic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vault {
    private long id;
    private String title;
    private User user;
    private List<Credential> credentials = new ArrayList<>();
    private boolean autoFill;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public boolean isAutoFill() {
        return autoFill;
    }

    public void setAutoFill(boolean autoFill) {
        this.autoFill = autoFill;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vault vault = (Vault) o;
        return id == vault.id && autoFill == vault.autoFill && Objects.equals(title, vault.title) && Objects.equals(user, vault.user) && Objects.equals(credentials, vault.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, autoFill);
    }
}
