package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarDao extends JpaRepository<CarEntity, Long>, CustomCarRepository {
    @Query("SELECT c FROM CarEntity c WHERE c.userEntity.id = :userId")
    List<CarEntity> findByUserId(long userId);
}
