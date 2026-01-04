package org.backwarden.api.logic.ports.input;

import org.backwarden.api.logic.model.User;

import java.util.List;

public interface UserUseCase {
    public long createUser(User user);

    public User getUser(long id);

    public User authenticate(String mail, String password);

}
