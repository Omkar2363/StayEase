package com.org.StayEase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


import com.org.StayEase.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
