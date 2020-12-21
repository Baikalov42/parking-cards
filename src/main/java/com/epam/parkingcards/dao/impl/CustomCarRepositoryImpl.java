package com.epam.parkingcards.dao.impl;

import com.epam.parkingcards.dao.CustomCarRepository;
import com.epam.parkingcards.model.Car;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomCarRepositoryImpl implements CustomCarRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Car updateCarWithoutUserId(Car car) {

        Car entityCar = entityManager.find(Car.class, car.getId());
        entityCar.setCarModel(car.getCarModel());
        entityCar.setLicensePlate(car.getLicensePlate());
        entityCar.setUser(car.getUser());

        entityManager.merge(entityCar);
        entityManager.flush();
        return entityCar;
    }
}
