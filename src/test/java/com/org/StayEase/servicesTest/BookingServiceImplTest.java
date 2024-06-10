package com.org.StayEase.servicesTest;

import com.org.StayEase.configs.JWTConfig.JWTHelper;
import com.org.StayEase.entities.Booking;
import com.org.StayEase.entities.Hotel;
import com.org.StayEase.entities.User;
import com.org.StayEase.exceptions.BookingNotFoundException;
import com.org.StayEase.exceptions.UserNotFoundException;
import com.org.StayEase.repositories.BookingRepository;
import com.org.StayEase.repositories.HotelRepository;
import com.org.StayEase.repositories.UserRepository;
import com.org.StayEase.services.Impl.BookingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTHelper jwtHelper;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;
    private Hotel hotel;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setAvailableRooms(10);

        booking = new Booking();
        booking.setId(1L);
        booking.setHotel(hotel);
        booking.setUser(user);
        booking.setBookingDate(new Date());

            }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getAllBookings();

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
    }

    @Test
    void testGetBookingById_BookingExists() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.getBookingById(1L);

        assertNotNull(foundBooking);
        assertEquals(1L, foundBooking.getId());
    }

    @Test
    void testGetBookingById_BookingNotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(1L));
    }

    @Test
    void testBookRoom_Success() {
        when(request.getHeader("Authorization")).thenReturn("Bearer testJwtToken");
        when(jwtHelper.getUsernameFromToken("testJwtToken")).thenReturn("test@example.com");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking bookedRoom = bookingService.bookRoom(1L);

        assertNotNull(bookedRoom);
        assertEquals("Test Hotel", bookedRoom.getHotel().getName());
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    void testBookRoom_UserNotFound() {
        when(request.getHeader("Authorization")).thenReturn("Bearer testJwtToken");
        when(jwtHelper.getUsernameFromToken("testJwtToken")).thenReturn("test@example.com");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bookingService.bookRoom(1L));
    }

    @Test
    void testBookRoom_HotelNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.bookRoom(1L));
    }

    @Test
    void testBookRoom_NoRoomsAvailable() {
        hotel.setAvailableRooms(0);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));

        assertThrows(RuntimeException.class, () -> bookingService.bookRoom(1L));
    }

    @Test
    void testCancelBooking_Success() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L);

        assertEquals(11, hotel.getAvailableRooms());
        verify(hotelRepository, times(1)).save(hotel);
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void testCancelBooking_BookingNotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.cancelBooking(1L));
    }
}
