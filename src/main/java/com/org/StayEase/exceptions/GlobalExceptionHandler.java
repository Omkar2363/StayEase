package com.org.StayEase.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeExceptionHandler(RuntimeException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundExceptionHandler(UserNotFoundException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<String> WeakPasswordExceptionHandler(WeakPasswordException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<String> HotelNotFoundExceptionHandler(HotelNotFoundException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }


    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<String> BookingNotFoundExceptionHandler(BookingNotFoundException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }


    @ExceptionHandler(HotelAlreadyExistException.class)
    public ResponseEntity<String> HotelAlreadyExistExceptionHandler(HotelAlreadyExistException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
