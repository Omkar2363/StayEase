package com.org.StayEase.services;


import com.org.StayEase.dtos.RegistrationDto;
import com.org.StayEase.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(RegistrationDto registrationDto);
    User getUserByEmail(String email);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(String emailId);
}
