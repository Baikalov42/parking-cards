package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDao extends JpaRepository<CarEntity, Long>, CustomCarRepository {
}
