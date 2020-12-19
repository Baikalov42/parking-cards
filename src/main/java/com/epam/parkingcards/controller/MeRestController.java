package com.epam.parkingcards.controller;

import com.epam.parkingcards.config.UserSecurity;
import com.epam.parkingcards.controller.mapper.CarBrandMapper;
import com.epam.parkingcards.controller.mapper.CarMapper;
import com.epam.parkingcards.controller.mapper.CarModelMapper;
import com.epam.parkingcards.controller.mapper.UserMapper;
import com.epam.parkingcards.controller.request.CarCreateRequest;
import com.epam.parkingcards.controller.request.CarRequest;
import com.epam.parkingcards.controller.request.UserRequest;
import com.epam.parkingcards.controller.response.CarBrandResponse;
import com.epam.parkingcards.controller.response.CarModelResponse;
import com.epam.parkingcards.controller.response.CarResponse;
import com.epam.parkingcards.controller.response.UserResponse;
import com.epam.parkingcards.model.Car;
import com.epam.parkingcards.model.User;
import com.epam.parkingcards.service.CarBrandService;
import com.epam.parkingcards.service.CarModelService;
import com.epam.parkingcards.service.CarService;
import com.epam.parkingcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/me")
public class MeRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @Autowired
    private CarBrandService carBrandService;
    @Autowired
    private CarModelService carModelService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CarModelMapper carModelMapper;
    @Autowired
    private CarBrandMapper carBrandMapper;

    @Autowired
    private UserSecurity userSecurity;


    @GetMapping
    public UserResponse getUser(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return userMapper.toUserResponse(user);
    }

    @PutMapping
    public UserResponse updateUser(@Valid @RequestBody UserRequest userRequest,
                                   Authentication authentication) {

        User user = userMapper.toUser(userRequest);
        long userId = userService.findIdByEmail(authentication.getName());
        user.setId(userId);

        return userMapper.toUserResponse(userService.update(user));
    }

    @GetMapping("/cars")
    public List<CarResponse> getMyCars(Principal principal) {

        List<Car> cars = userService.findCarsByUserEmail(principal.getName());
        return carMapper.toCarResponses(cars);
    }


    @PostMapping("/cars")
    public long addCar(@Valid @RequestBody CarCreateRequest carCreateRequest,
                       Authentication authentication) {

        Car car = carMapper.toCar(carCreateRequest);
        long userId = userService.findIdByEmail(authentication.getName());
        car.getUser().setId(userId);

        return carService.create(car);
    }

    @PutMapping("/cars")
    public CarResponse updateMyCarById(@Valid @RequestBody CarRequest carRequest,
                                       Authentication authentication) {

        System.err.println(carRequest);
        if (!userSecurity.hasCarId(authentication, carRequest.getId())) {
            throw new ValidationException("user dont have car, with id " + carRequest.getId());
        }
        long userId = userService.findIdByEmail(authentication.getName());

        Car forUpdate = carMapper.toCar(carRequest);
        forUpdate.getUser().setId(userId);

        Car updated = carService.update(forUpdate);
        return carMapper.toCarResponse(updated);
    }

    @DeleteMapping("/cars/{carId}")
    public void deleteMyCarById(@PathVariable long carId,
                                Authentication authentication) {

        if (!userSecurity.hasCarId(authentication, carId)) {
            throw new ValidationException("user dont have car, with id " + carId);
        }
        System.err.println(carId);
        carService.deleteById(carId);
    }

    @GetMapping("/models/page/{pageNumber}")
    public List<CarModelResponse> getAllModels(@PathVariable int pageNumber) {
        return carModelMapper.toCarModelResponses(carModelService.findAll(pageNumber));
    }

    @GetMapping("/brands/page/{pageNumber}")
    public List<CarBrandResponse> getAllBrands(@PathVariable int pageNumber) {
        return carBrandMapper.toCarBrandResponses(carBrandService.findAll(pageNumber));
    }
}
