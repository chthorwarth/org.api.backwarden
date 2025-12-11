package org.backwarden.api.adapters.controller.model;

import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;

import java.util.List;

public class VaultDTO {
    private long id;
    private String title;
    private List<CredentialDTO> credentials;
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

    public List<CredentialDTO> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialDTO> credentials) {
        this.credentials = credentials;
    }

    public boolean isAutoFill() {
        return autoFill;
    }

    public void setAutoFill(boolean autoFill) {
        this.autoFill = autoFill;
    }
}
