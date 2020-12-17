package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(nativeQuery = true,
    value = "SELECT u.user_id, first_name, last_name, phone," +
            " email, password FROM users as u JOIN cars as c " +
            "on u.user_id = c.user_id where c.license_plate = :licensePlate")
    Optional<User> findByLicensePlate(String licensePlate);
}
