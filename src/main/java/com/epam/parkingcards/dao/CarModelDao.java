package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarBrand;
import com.epam.parkingcards.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarModelDao extends JpaRepository<CarModel, Long> {
    public List<CarModel> findByCarBrand(CarBrand carBrand);

}
