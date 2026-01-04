package org.backwarden.api.logic.exceptions;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException(String message) {

        super(message);
    }
}
