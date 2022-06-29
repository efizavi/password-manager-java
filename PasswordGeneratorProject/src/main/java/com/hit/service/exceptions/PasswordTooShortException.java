package com.hit.service.exceptions;

public class PasswordTooShortException extends RuntimeException{
    public PasswordTooShortException() {
        super("The chosen password was too short.");
    }
}
