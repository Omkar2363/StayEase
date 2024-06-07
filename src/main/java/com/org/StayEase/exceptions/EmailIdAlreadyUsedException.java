package com.org.StayEase.exceptions;

public class EmailIdAlreadyUsedException extends RuntimeException {

    public EmailIdAlreadyUsedException(String message){
        super(message);
    }
}
