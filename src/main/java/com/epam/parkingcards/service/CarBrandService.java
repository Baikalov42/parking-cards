package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarBrandDao;
import com.epam.parkingcards.model.CarBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CarBrandService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CarBrandDao carBrandDao;

    public long create(CarBrand carBrand) {
        return 0;
    }

    public CarBrand findById(long id) {
        return null;
    }

    public Collection<CarBrand> findAll(int pageNumber) {
        return null;
    }

    public CarBrand update(CarBrand carBrand) {
        return null;
    }

    public void deleteById(long id) {

    }
}
