package com.org.StayEase.services.Impl;

import com.org.StayEase.configs.JWTConfig.JWTHelper;
import com.org.StayEase.entities.Booking;
import com.org.StayEase.entities.Hotel;
import com.org.StayEase.entities.User;
import com.org.StayEase.exceptions.BookingNotFoundException;
import com.org.StayEase.exceptions.UserNotFoundException;
import com.org.StayEase.repositories.BookingRepository;
import com.org.StayEase.repositories.HotelRepository;
import com.org.StayEase.repositories.UserRepository;
import com.org.StayEase.services.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTHelper jwtHelper;

    @Override
    public List<Booking> getAllBookings() {
        logger.info("Getting all bookings");
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        logger.info("Getting booking by ID: {}", bookingId);
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking with ID '{}' not found", bookingId);
                    return new BookingNotFoundException("Provide a Valid BookingId.");
                });
    }

    @Override
    public Booking bookRoom(Long hotelId) {
        logger.info("Booking room for hotel with ID: {}", hotelId);

        String jwtToken = getJwtFromRequest();
        String userName = this.jwtHelper.getUsernameFromToken(jwtToken);

        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> {
                    logger.error("User with email '{}' not found", userName);
                    return new UserNotFoundException("Register First to BookRoom.");
                });

        logger.debug("Getting user from the JWT: {}, {}", jwtToken, userName);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                    logger.error("Hotel with ID '{}' not found", hotelId);
                    return new RuntimeException("Hotel with given hotelId is not available");
                });

        if (hotel.getAvailableRooms() > 0) {
            hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
            hotelRepository.save(hotel);

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setHotel(hotel);
            booking.setBookingDate(new Date());

            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Room booked with booking ID: {}", savedBooking.getId());
            return savedBooking;
        } else {
            logger.warn("No rooms available for hotel with ID: {}", hotelId);
            throw new RuntimeException("No rooms available");
        }
    }

    @Override
    public void cancelBooking(Long bookingId) {
        logger.info("Canceling booking with ID: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking with ID '{}' not found", bookingId);
                    return new RuntimeException("Booking not found, Provide a Valid BookingId");
                });
        Hotel hotel = booking.getHotel();
        hotel.setAvailableRooms(hotel.getAvailableRooms() + 1);
        hotelRepository.save(hotel);
        bookingRepository.delete(booking);
        logger.info("Booking with ID '{}' canceled", bookingId);
    }

    // Helper method to extract JWT token from request header
    private String getJwtFromRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
        }
        return null;
    }

}