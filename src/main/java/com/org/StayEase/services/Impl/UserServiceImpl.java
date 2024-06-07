package com.org.StayEase.services.Impl;

import com.org.StayEase.dtos.RegistrationDto;
import com.org.StayEase.entities.Role;
import com.org.StayEase.entities.User;
import com.org.StayEase.exceptions.EmailIdAlreadyUsedException;
import com.org.StayEase.exceptions.WeakPasswordException;
import com.org.StayEase.repositories.UserRepository;
import com.org.StayEase.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found...!!!"));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found...!!!"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User createUser(RegistrationDto registrationDto) {
        User user = modelMapper.map(registrationDto, User.class);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailIdAlreadyUsedException("Email id already registered...");
        }

        //Checks on the Password :
        String password = user.getPassword();
        if(password == null || password.length() < 8){
            throw new WeakPasswordException("The password must have at least 8 characters");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.CUSTOMER);
        User registeredUser = userRepository.save(user);
        return registeredUser;
    }



    @Override
    public User updateUser(User user) {
        User oldUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found...!!!"));
        user.setId(oldUser.getId());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found...!!!"));
        System.out.println("\n\nUser : "+user);
        userRepository.delete(user);
    }


}
