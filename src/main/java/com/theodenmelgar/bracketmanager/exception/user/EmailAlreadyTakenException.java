package com.theodenmelgar.bracketmanager.exception.user;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException(String email){
        super(email + " is already connected to another account. Please select a new email.");
    }
}
