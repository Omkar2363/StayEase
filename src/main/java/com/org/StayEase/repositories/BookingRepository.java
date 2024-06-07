package com.org.StayEase.repositories;


import com.org.StayEase.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, Long> {

}
