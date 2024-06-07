package com.org.StayEase.exceptions;

public class HotelAlreadyExistException extends RuntimeException {

    public HotelAlreadyExistException(String message){
        super(message);
    }
}
