package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarEntity;

public interface CustomCarRepository {
    CarEntity updateCarWithoutUserId(CarEntity carEntity);
}
