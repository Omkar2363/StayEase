package com.org.StayEase.exceptions;

public class WeakPasswordException extends RuntimeException {

    public WeakPasswordException(String message){
        super(message);
    }
}
