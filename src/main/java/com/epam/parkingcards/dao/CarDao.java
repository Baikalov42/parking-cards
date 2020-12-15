package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CarDao extends JpaRepository<Car, Long> {
}
