package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarModelDao;
import com.epam.parkingcards.model.CarModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CarModelService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CarModelDao carModelDao;

    public long create(CarModel carModel) {
        return 0;
    }

    public CarModel findById(long id) {
        return null;
    }

    public Collection<CarModel> findAll(int pageNumber) {
        return null;
    }

    public Collection<CarModel> findAllByBrand(long brandId, int pageNumber) {
        return null;
    }

    public CarModel update(CarModel carModel) {
        return null;
    }

    public void deleteById(long id) {

    }
}
