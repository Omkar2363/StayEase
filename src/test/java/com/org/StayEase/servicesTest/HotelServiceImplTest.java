package com.org.StayEase.servicesTest;


import com.org.StayEase.entities.Hotel;
import com.org.StayEase.exceptions.HotelAlreadyExistException;
import com.org.StayEase.exceptions.HotelNotFoundException;
import com.org.StayEase.repositories.HotelRepository;
import com.org.StayEase.services.Impl.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
    }

    @Test
    void testCreateHotel_Success() {
        when(hotelRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        Hotel createdHotel = hotelService.createHotel(hotel);

        assertNotNull(createdHotel);
        assertEquals("Test Hotel", createdHotel.getName());
    }

    @Test
    void testCreateHotel_HotelAlreadyExists() {
        when(hotelRepository.findByName(anyString())).thenReturn(Optional.of(hotel));

        assertThrows(HotelAlreadyExistException.class, () -> hotelService.createHotel(hotel));
    }

    @Test
    void testGetHotelById_HotelExists() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));

        Hotel foundHotel = hotelService.getHotelById(1L);

        assertNotNull(foundHotel);
        assertEquals(1L, foundHotel.getId());
    }

    @Test
    void testGetHotelById_HotelNotFound() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.getHotelById(1L));
    }

    @Test
    void testGetAllHotels() {
        when(hotelRepository.findAll()).thenReturn(Arrays.asList(hotel));

        List<Hotel> hotels = hotelService.getAllHotels();

        assertNotNull(hotels);
        assertEquals(1, hotels.size());
    }

    @Test
    void testUpdateHotel_Success() {
        Hotel updateHotel = new Hotel();
        updateHotel.setName("Updated Hotel");
        updateHotel.setLocation("Updated Location");


        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(updateHotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(updateHotel);

        Hotel updatedHotel = hotelService.updateHotel(1L, updateHotel);

        assertNotNull(updatedHotel);
        assertEquals("Updated Hotel", updatedHotel.getName());
    }

    @Test
    void testUpdateHotel_HotelNotFound() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        Hotel updateHotel = new Hotel();
        updateHotel.setName("Updated Hotel");
        updateHotel.setLocation("Updated Location");

        assertThrows(HotelNotFoundException.class, () -> hotelService.updateHotel(1L, updateHotel));
    }

    @Test
    void testDeleteHotel_Success() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));

        hotelService.deleteHotel(1L);

        verify(hotelRepository, times(1)).delete(hotel);
    }

    @Test
    void testDeleteHotel_HotelNotFound() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.deleteHotel(1L));
    }
}

