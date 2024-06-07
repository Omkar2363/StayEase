package com.org.StayEase.controllers;

import com.org.StayEase.entities.User;
import com.org.StayEase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired

    public UserService userService;


    @PreAuthorize("hasAnyRole('ADMIN','HOTEL_MANAGER')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HOTEL_MANAGER')")
    @GetMapping("/emailId")
    public ResponseEntity<User> getUserByEmail(@RequestBody String email){
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HOTEL_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN','HOTEL_MANAGER','CUSTOMER')")
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HOTEL_MANAGER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        System.out.println("\nEmail : "+userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>("User with userId "+userId+" has been removed successfully...!!!", HttpStatus.OK);
    }

}

