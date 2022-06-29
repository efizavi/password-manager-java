package com.hit.service.exceptions;

public class UsernameTakenException extends RuntimeException{
    public UsernameTakenException() {
        super("The selected username is already taken");
    }
}
