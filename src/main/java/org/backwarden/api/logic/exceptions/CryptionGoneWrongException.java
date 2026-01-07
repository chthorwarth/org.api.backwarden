package org.backwarden.api.logic.exceptions;

public class CryptionGoneWrongException extends RuntimeException {

    public CryptionGoneWrongException(String message) {
        super(message);
    }
}
