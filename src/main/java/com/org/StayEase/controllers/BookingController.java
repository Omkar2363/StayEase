package com.org.StayEase.controllers;


import com.org.StayEase.entities.Booking;
import com.org.StayEase.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/hotels/{hotelId}/book")
    @PreAuthorize("hasAnyRole('CUSTOMER','HOTEL_MANAGER','ADMIN')")
    public ResponseEntity<Booking> bookRoom(@PathVariable Long hotelId) {
        Booking booking = bookingService.bookRoom(hotelId);
        return ResponseEntity.ok().body(booking);
    }


    @DeleteMapping("/bookings/{bookingId}")
    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking with BookingId "+bookingId+" has been cancelled successfully...!!!");
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasAnyRole('HOTEL_MANAGER','ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings(){
        return ResponseEntity.ok().body(bookingService.getAllBookings());
    }

    @GetMapping("/bookings/{bookingId}")
    @PreAuthorize("hasAnyRole('HOTEL_MANAGER','ADMIN')")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long bookingId){
        return ResponseEntity.ok().body(bookingService.getBookingById(bookingId));
    }
}

