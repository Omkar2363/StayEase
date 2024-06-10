package com.org.StayEase.services.Impl;

import com.org.StayEase.dtos.RegistrationDto;
import com.org.StayEase.entities.Role;
import com.org.StayEase.entities.User;
import com.org.StayEase.exceptions.EmailIdAlreadyUsedException;
import com.org.StayEase.exceptions.WeakPasswordException;
import com.org.StayEase.repositories.UserRepository;
import com.org.StayEase.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User getUserByEmail(String email) {
        logger.info("Getting user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", email);
                    return new UsernameNotFoundException("User Not Found...!!!");
                });
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Getting user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found for id: {}", id);
                    return new UsernameNotFoundException("User Not Found...!!!");
                });
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Getting all users");
        return userRepository.findAll();
    }

    @Override
    public User createUser(RegistrationDto registrationDto) {
        logger.info("Creating user with email: {}", registrationDto.getEmail());
        User user = modelMapper.map(registrationDto, User.class);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("Email id {} already registered", user.getEmail());
            throw new EmailIdAlreadyUsedException("Email id already registered...");
        }

        String password = user.getPassword();
        if (password == null || password.length() < 8) {
            logger.error("Weak password provided for user: {}", user.getEmail());
            throw new WeakPasswordException("The password must have at least 8 characters");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.CUSTOMER);
        User registeredUser = userRepository.save(user);
        logger.info("User created with id: {}", registeredUser.getId());
        return registeredUser;
    }

    @Override
    public User updateUser(User user) {
        logger.info("Updating user with email: {}", user.getEmail());
        User oldUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", user.getEmail());
                    return new UsernameNotFoundException("User Not Found...!!!");
                });
        user.setId(oldUser.getId());
        User updatedUser = userRepository.save(user);
        logger.info("User updated with id: {}", updatedUser.getId());
        return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        logger.info("Deleting user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found for id: {}", userId);
                    return new UsernameNotFoundException("User Not Found...!!!");
                });
        userRepository.delete(user);
        logger.info("User deleted with id: {}", userId);
    }
}