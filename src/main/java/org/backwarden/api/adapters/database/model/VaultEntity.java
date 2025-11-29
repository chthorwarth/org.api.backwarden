package org.backwarden.api.adapters.database.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;

@Entity
public class VaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    //private UserEntity user;
    //private List<CredentialEntity> credentials;
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

//    public UserEntity getUser() {
//        return user;
//    }
//
//    public void setUser(UserEntity user) {
//        this.user = user;
//    }

//    public List<CredentialEntity> getCredentials() {
//        return credentials;
//    }
//
//    public void setCredentials(List<CredentialEntity> credentials) {
//        this.credentials = credentials;
//    }

    public boolean isAutoFill() {
        return autoFill;
    }

    public void setAutoFill(boolean autoFill) {
        this.autoFill = autoFill;
    }
}
