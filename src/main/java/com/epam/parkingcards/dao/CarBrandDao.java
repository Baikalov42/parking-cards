package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarBrand;
import org.springframework.data.repository.CrudRepository;

public interface CarBrandDao extends CrudRepository<CarBrand, Long> {
}
