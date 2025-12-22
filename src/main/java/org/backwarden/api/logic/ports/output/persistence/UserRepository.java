package org.backwarden.api.logic.ports.output.persistence;


import org.backwarden.api.logic.model.User;

import java.util.List;

public interface UserRepository
{
    public void saveUser(User user);

    public User getUser(long id);

}
