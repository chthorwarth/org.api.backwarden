package org.backwarden.api.logic.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {

        super(message);
    }
}
