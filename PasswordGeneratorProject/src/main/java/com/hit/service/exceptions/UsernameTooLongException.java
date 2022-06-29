package com.hit.service.exceptions;

public class UsernameTooLongException extends RuntimeException{
    public UsernameTooLongException() {
        super("The chosen username was too long.");
    }
}
