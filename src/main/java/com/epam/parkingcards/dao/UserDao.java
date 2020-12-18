package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.cars c WHERE c.licensePlate = :licensePlate")
    Optional<User> findByLicensePlate(String licensePlate);
}
