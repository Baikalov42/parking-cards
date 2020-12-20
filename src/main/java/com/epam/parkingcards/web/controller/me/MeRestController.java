package com.epam.parkingcards.web.controller.me;

import com.epam.parkingcards.config.UserSecurity;
import com.epam.parkingcards.web.mapper.CarBrandMapper;
import com.epam.parkingcards.web.mapper.CarMapper;
import com.epam.parkingcards.web.mapper.CarModelMapper;
import com.epam.parkingcards.web.mapper.UserMapper;
import com.epam.parkingcards.web.request.me.MeCarCreateRequest;
import com.epam.parkingcards.web.request.me.MeCarUpdateRequest;
import com.epam.parkingcards.web.request.me.MeUserUpdateRequest;
import com.epam.parkingcards.web.response.CarBrandResponse;
import com.epam.parkingcards.web.response.CarModelResponse;
import com.epam.parkingcards.web.response.CarResponse;
import com.epam.parkingcards.web.response.UserResponse;
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
import java.util.ArrayList;
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
    public UserResponse updateUser(@Valid @RequestBody MeUserUpdateRequest meUserUpdateRequest,
                                   Authentication authentication) {

        User user = userMapper.toUser(meUserUpdateRequest);
        long userId = userService.getIdByEmail(authentication.getName());
        user.setId(userId);

        return userMapper.toUserResponse(userService.update(user));
    }

    @GetMapping("/cars")
    public List<CarResponse> getMyCars(Principal principal) {

        User user = userService.findByEmail(principal.getName());
        List<Car> cars = new ArrayList<>(user.getCars());
        return carMapper.toCarResponses(cars);
    }

    @PostMapping("/cars")
    public long addCar(@Valid @RequestBody MeCarCreateRequest meCarCreateRequest,
                       Authentication authentication) {

        Car car = carMapper.toCar(meCarCreateRequest);
        long userId = userService.getIdByEmail(authentication.getName());
        car.getUser().setId(userId);

        return carService.create(car);
    }

    @PutMapping("/cars")
    public CarResponse updateMyCarById(@Valid @RequestBody MeCarUpdateRequest meCarUpdateRequest,
                                       Authentication authentication) {

        if (!userSecurity.hasCar(authentication, meCarUpdateRequest.getId())) {
            throw new ValidationException("user dont have car, with id " + meCarUpdateRequest.getId());
        }
        long userId = userService.getIdByEmail(authentication.getName());

        Car toUpdate = carMapper.toCar(meCarUpdateRequest);
        toUpdate.getUser().setId(userId);

        Car updated = carService.update(toUpdate);
        return carMapper.toCarResponse(updated);
    }

    @DeleteMapping("/cars/{carId}")
    public void deleteMyCarById(@PathVariable long carId,
                                Authentication authentication) {

        if (!userSecurity.hasCar(authentication, carId)) {
            throw new ValidationException("user dont have car, with id " + carId);
        }
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
