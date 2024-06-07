package com.org.StayEase.services;

import com.org.StayEase.entities.Hotel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface HotelService {

    Hotel createHotel(Hotel hotel);
    List<Hotel> getAllHotels();
    Hotel getHotelById(Long hotelId);
    Hotel updateHotel(Long hotelId, Hotel hotel);
    void deleteHotel(Long hotelId);

}
