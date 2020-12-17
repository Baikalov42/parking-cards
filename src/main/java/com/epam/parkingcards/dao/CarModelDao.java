package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelDao extends JpaRepository<CarModel, Long> {
}
