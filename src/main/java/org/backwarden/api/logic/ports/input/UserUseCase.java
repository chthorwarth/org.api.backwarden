package org.backwarden.api.logic.ports.input;

import org.backwarden.api.logic.model.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserUseCase {
    public long createUser(User user);

    public User getUser(long id);

    public User authenticate(String mail, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

    public String generateJWTandKDF(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

}
