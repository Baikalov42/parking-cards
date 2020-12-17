package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarDao;
import com.epam.parkingcards.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CarService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CarDao carDao;

    public long create(Car car) {
        return 0;
    }

    public Car findById(long id) {
        return null;
    }

    public Collection<Car> findAll(int pageNumber) {
        return null;
    }

    public Car update(Car car) {
        return null;
    }

    public void deleteById(long id) {

    }
}
