package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.BrandDao;
import com.epam.parkingcards.model.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BrandService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private BrandDao brandDao;

    public long create(Brand brand) {
        return 0;
    }

    public Brand findById(long id) {
        return null;
    }

    public Collection<Brand> findAll(int pageNumber) {
        return null;
    }

    public Brand update(Brand brand) {
        return null;
    }

    public void deleteById(long id) {

    }
}
