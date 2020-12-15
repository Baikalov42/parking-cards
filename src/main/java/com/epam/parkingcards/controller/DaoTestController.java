package com.epam.parkingcards.controller;

import com.epam.parkingcards.dao.CarDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.model.Car;
import com.epam.parkingcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/test")
public class DaoTestController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CarDao carDao;

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userDao.findAll();
    }

    @GetMapping("/cars")
    public Iterable<Car> getAllCars() {
        return carDao.findAll();
    }
    //TODO: delete when implementing REST interface
}
