package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<UserEntity, Long>, CustomUserRepository {

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u "
            + "FROM UserEntity u "
            + "JOIN u.carEntities c "
            + "WHERE c.licensePlate = :licensePlate")
    Optional<UserEntity> findByLicensePlate(String licensePlate);

    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.firstName) LIKE %:keyword% OR LOWER(u.lastName) LIKE %:keyword%")
    List<UserEntity> findByKeyword(String keyword);
}
