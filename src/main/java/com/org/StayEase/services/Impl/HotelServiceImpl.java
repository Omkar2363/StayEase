package com.org.StayEase.services.Impl;

import com.org.StayEase.entities.Hotel;
import com.org.StayEase.exceptions.HotelAlreadyExistException;
import com.org.StayEase.exceptions.HotelNotFoundException;
import com.org.StayEase.repositories.HotelRepository;
import com.org.StayEase.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;


    @Override
    public Hotel createHotel(Hotel hotel) {
        if (hotelRepository.findByName(hotel.getName()).isPresent()) {
            throw new HotelAlreadyExistException("Hotel with given hotel name already exist. Use some other name...!!!");
        }
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(()->new HotelNotFoundException("Hotel with hotelId "+hotelId+" is not available"));
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel updateHotel(Long hotelId, Hotel updateHotel) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()->new HotelNotFoundException("Hotel with hotelId "+hotelId+" is not available"));

        updateHotel.setId(hotel.getId());
        Hotel updatedHotel = hotelRepository.save(updateHotel);
        return updatedHotel;
    }

    @Override
    public void deleteHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()->new HotelNotFoundException("Hotel with hotelId "+hotelId+" is not available"));

        hotelRepository.delete(hotel);
    }
}
