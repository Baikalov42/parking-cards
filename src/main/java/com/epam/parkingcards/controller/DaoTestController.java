package com.epam.parkingcards.controller;

import com.epam.parkingcards.dao.BrandDao;
import com.epam.parkingcards.dao.CarDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.model.Car;
import com.epam.parkingcards.model.Brand;
import com.epam.parkingcards.model.Role;
import com.epam.parkingcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class DaoTestController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CarDao carDao;
    @Autowired
    private BrandDao brandDao;

    @GetMapping("/users")
    public String getAllUsers() {
        List<User> all = userDao.findAll();

        StringBuilder stringBuilder = new StringBuilder();
        for (User user : all) {
            stringBuilder.append(user.getEmail());
            stringBuilder.append(" ");
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                stringBuilder.append(role.getName());
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        return all.toString();
    }

    @GetMapping("/cars")
    public String getAllCars() {
        StringBuilder stringBuilder = new StringBuilder();

        List<Car> all = carDao.findAll();
        for (Car car : all) {
            stringBuilder.append(car.getLicensePlate());
            stringBuilder.append(" ");
            stringBuilder.append(car.getId());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @GetMapping("/brands")
    public Iterable<Brand> getAllBrands() {
        return brandDao.findAll();
    }

    @GetMapping("/admin")
    public String admin() {
        return "hi admin";
    }

    @GetMapping("/index")
    public String index() {
        return "hello guest";
    }

    @GetMapping("/me")
    public String forUser() {
        return "hello me";
    }
}
