package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.ModelDao;
import com.epam.parkingcards.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ModelService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ModelDao modelDao;

    public long create(Model model) {
        return 0;
    }

    public Model findById(long id) {
        return null;
    }

    public Collection<Model> findAll(int pageNumber) {
        return null;
    }

    public Collection<Model> findAllByBrand(long brandId, int pageNumber) {
        return null;
    }

    public Model update(Model model) {
        return null;
    }

    public void deleteById(long id) {

    }
}
