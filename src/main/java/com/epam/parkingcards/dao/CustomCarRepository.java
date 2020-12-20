package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.Car;

public interface CustomCarRepository {
    Car updateCarWithoutUserId(Car car);
}
