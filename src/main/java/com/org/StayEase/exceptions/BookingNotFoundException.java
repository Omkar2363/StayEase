package com.org.StayEase.exceptions;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(String message){
        super(message);
    }
}
