package com.theodenmelgar.bracketmanager.exception.user;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super("Old password does not match existing password");
    }

}
