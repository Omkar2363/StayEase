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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

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
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(()-> new BookingNotFoundException("Provide a Valid BookingId."));
    }

    @Override
    public Booking bookRoom(Long hotelId) {

        //Extract Jwt token from the Authorization header :
        String jwtToken = getJwtFromRequest();
        String userName = this.jwtHelper.getUsernameFromToken(jwtToken);

        User user = userRepository.findByEmail(userName).orElseThrow(()->new UserNotFoundException("Register First to BookRoom."));

        System.out.println("\n\nGetting user from the Jwt");
        System.out.println("JwtToken : "+jwtToken);
        System.out.println("userName : "+userName);
        System.out.println("User     : "+user);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel with given hotelId is not available"));

        if (hotel.getAvailableRooms() > 0) {
            hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
            hotelRepository.save(hotel);

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setHotel(hotel);
            booking.setBookingDate(new Date());

            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("No rooms available");
        }
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found, Provide a Valid BookingId"));
        Hotel hotel = booking.getHotel();
        hotel.setAvailableRooms(hotel.getAvailableRooms() + 1);
        hotelRepository.save(hotel);
        bookingRepository.delete(booking);
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
