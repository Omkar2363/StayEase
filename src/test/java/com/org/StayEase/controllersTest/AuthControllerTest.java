package com.org.StayEase.controllersTest;

import com.org.StayEase.configs.JWTConfig.JWTHelper;
import com.org.StayEase.controllers.AuthController;
import com.org.StayEase.dtos.LoginRequestDto;
import com.org.StayEase.dtos.LoginResponseDto;
import com.org.StayEase.dtos.RegistrationDto;
import com.org.StayEase.entities.User;
import com.org.StayEase.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTHelper jwtHelper;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser() {
        RegistrationDto registrationDto = new RegistrationDto();
        User user = new User();

        when(userService.createUser(any(RegistrationDto.class))).thenReturn(user);

        ResponseEntity<User> response = authController.registerUser(registrationDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).createUser(any(RegistrationDto.class));
    }

    @Test
    void loginUser() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        String jwtToken = "jwtToken";

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtHelper.generateToken(any(UserDetails.class))).thenReturn(jwtToken);

        ResponseEntity<LoginResponseDto> response = authController.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtToken, response.getBody().getJwtToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
