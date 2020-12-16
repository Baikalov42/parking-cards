package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.ModelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    @Autowired
    private ModelDao modelDao;
}
