package com.theodenmelgar.bracketmanager.exception.user;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String username){
        super("Username " + username + " is already taken. Select a new username.");
    }
}
