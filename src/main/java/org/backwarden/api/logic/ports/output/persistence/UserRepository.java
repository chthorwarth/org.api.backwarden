package org.backwarden.api.logic.ports.output.persistence;


import org.backwarden.api.logic.model.User;

import java.util.List;

public interface UserRepository {
    public long saveUser(User user);

    public User getUser(long id);

    public User getUser(String mail);

    public void deleteAll();

}
