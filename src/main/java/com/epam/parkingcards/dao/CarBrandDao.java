package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarBrandDao extends JpaRepository<CarBrand, Long> {
}
