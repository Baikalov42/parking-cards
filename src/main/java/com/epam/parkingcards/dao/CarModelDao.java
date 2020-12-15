package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarModel;
import org.springframework.data.repository.CrudRepository;

public interface CarModelDao extends CrudRepository<CarModel, Long> {
}
