package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
