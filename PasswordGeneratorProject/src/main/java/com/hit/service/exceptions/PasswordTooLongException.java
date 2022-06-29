package com.hit.service.exceptions;

public class PasswordTooLongException extends RuntimeException{
    public PasswordTooLongException() {
        super("The chosen password was too long.");
    }
}
