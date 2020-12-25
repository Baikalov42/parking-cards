package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarDao extends JpaRepository<CarEntity, Long>, CustomCarRepository {
//TODO SECURITY: Можно без optional, если не найдено - вернется пустой лист
    @Query("SELECT c FROM CarEntity c WHERE c.userEntity.id = :userId")
    Optional<List<CarEntity>> findByUserId(long userId);
}
