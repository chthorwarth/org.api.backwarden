package org.backwarden.api.logic.exceptions;

public class DomainValidationException extends RuntimeException{

    public DomainValidationException(String message){
        super(message);
    }
}
