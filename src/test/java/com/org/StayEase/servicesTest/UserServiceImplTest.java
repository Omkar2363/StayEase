package com.org.StayEase.servicesTest;

import com.org.StayEase.dtos.RegistrationDto;
import com.org.StayEase.entities.Role;
import com.org.StayEase.entities.User;
import com.org.StayEase.repositories.UserRepository;
import com.org.StayEase.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private RegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.CUSTOMER);

        registrationDto = new RegistrationDto();
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password");
    }

    @Test
    void testGetUserByEmail_UserExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(modelMapper.map(any(RegistrationDto.class), eq(User.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(registrationDto);

        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
    }



    @Test
    void testUpdateUser_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
