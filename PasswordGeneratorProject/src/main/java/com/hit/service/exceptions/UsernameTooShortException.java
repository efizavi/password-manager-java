package com.hit.service.exceptions;

public class UsernameTooShortException extends RuntimeException{
    public UsernameTooShortException() {
        super("The chosen username was too short.");
    }
}
