package com.epam.parkingcards.dao;

import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CarDaoTest {
    @Autowired
    CarDao carDao;

    @Test
    public void when_carUpdated_then_nestedObjectsNotNull() {
        Car entityCar = carDao.findById(1L).orElseThrow(() -> new NotFoundException("There is no Car with Id 1"));
        System.err.println(entityCar);


    }
}
