package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDao extends JpaRepository<Car, Long>, CustomCarRepository {
}
