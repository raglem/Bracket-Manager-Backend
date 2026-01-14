package com.theodenmelgar.bracketmanager.exception.user;

public class InvalidUserCredentialsException extends RuntimeException {

    public InvalidUserCredentialsException(){
        super("No user exists with the matching username and password");
    }

    public InvalidUserCredentialsException(String msg){
        super(msg);
    }
}
