package com.org.StayEase.services.Impl;

import com.org.StayEase.entities.Hotel;
import com.org.StayEase.exceptions.HotelAlreadyExistException;
import com.org.StayEase.exceptions.HotelNotFoundException;
import com.org.StayEase.repositories.HotelRepository;
import com.org.StayEase.services.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        logger.info("Creating hotel: {}", hotel.getName());
        if (hotelRepository.findByName(hotel.getName()).isPresent()) {
            logger.error("Hotel with name '{}' already exists", hotel.getName());
            throw new HotelAlreadyExistException("Hotel with given hotel name already exist. Use some other name...!!!");
        }
        Hotel createdHotel = hotelRepository.save(hotel);
        logger.info("Hotel created with ID: {}", createdHotel.getId());
        return createdHotel;
    }

    @Override
    public Hotel getHotelById(Long hotelId) {
        logger.info("Getting hotel by ID: {}", hotelId);
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                    logger.error("Hotel with ID '{}' not found", hotelId);
                    return new HotelNotFoundException("Hotel with hotelId " + hotelId + " is not available");
                });
    }

    @Override
    public List<Hotel> getAllHotels() {
        logger.info("Getting all hotels");
        return hotelRepository.findAll();
    }

    @Override
    public Hotel updateHotel(Long hotelId, Hotel updateHotel) {
        logger.info("Updating hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                    logger.error("Hotel with ID '{}' not found", hotelId);
                    return new HotelNotFoundException("Hotel with hotelId " + hotelId + " is not available");
                });

        updateHotel.setId(hotel.getId());
        Hotel updatedHotel = hotelRepository.save(updateHotel);
        logger.info("Hotel with ID '{}' updated", updatedHotel.getId());
        return updatedHotel;
    }

    @Override
    public void deleteHotel(Long hotelId) {
        logger.info("Deleting hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                    logger.error("Hotel with ID '{}' not found", hotelId);
                    return new HotelNotFoundException("Hotel with hotelId " + hotelId + " is not available");
                });

        hotelRepository.delete(hotel);
        logger.info("Hotel with ID '{}' deleted", hotelId);
    }
}