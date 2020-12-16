package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    @Autowired
    private CarDao carDao;


}
