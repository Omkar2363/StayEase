package com.org.StayEase.controllers;


import com.org.StayEase.entities.Hotel;
import com.org.StayEase.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        return ResponseEntity.ok().body(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long hotelId) {
        return ResponseEntity.ok().body(hotelService.getHotelById(hotelId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok().body(hotelService.createHotel(hotel));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{hotelId}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long hotelId) {
        hotelService.deleteHotel(hotelId);
        return new ResponseEntity<>("Hotel with hotelId "+hotelId+" has been removed successfully...!!!", HttpStatus.OK);
    }

    @PutMapping("/{hotelId}")
    @PreAuthorize("hasAnyRole('ADMIN','HOTEL_MANAGER')")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long hotelId, @RequestBody Hotel hotelDetails) {
        return ResponseEntity.ok().body(hotelService.updateHotel(hotelId, hotelDetails));
    }


}

