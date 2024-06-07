package com.org.StayEase.services;

import com.org.StayEase.entities.Booking;
import java.util.List;


public interface BookingService {

    Booking bookRoom(Long hotelId);
    List<Booking> getAllBookings();
    Booking getBookingById(Long bookingId);
    void cancelBooking(Long bookingId);



}

