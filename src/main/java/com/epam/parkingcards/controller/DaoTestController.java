package com.epam.parkingcards.controller;

import com.epam.parkingcards.controller.mapper.CarBrandMapper;
import com.epam.parkingcards.controller.mapper.CarMapper;
import com.epam.parkingcards.controller.mapper.CarModelMapper;
import com.epam.parkingcards.controller.mapper.UserMapper;
import com.epam.parkingcards.controller.request.CarBrandRequest;
import com.epam.parkingcards.controller.request.CarModelRequest;
import com.epam.parkingcards.controller.request.UserCreateRequest;
import com.epam.parkingcards.controller.request.UserRequest;
import com.epam.parkingcards.controller.response.CarBrandResponse;
import com.epam.parkingcards.controller.response.CarModelResponse;
import com.epam.parkingcards.controller.response.CarResponse;
import com.epam.parkingcards.controller.response.UserResponse;
import com.epam.parkingcards.dao.CarBrandDao;
import com.epam.parkingcards.dao.CarDao;
import com.epam.parkingcards.dao.CarModelDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.model.User;
import com.epam.parkingcards.service.CarBrandService;
import com.epam.parkingcards.service.CarModelService;
import com.epam.parkingcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.security.Principal;
import java.util.List;

@RestController
public class DaoTestController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CarDao carDao;
    @Autowired
    private CarBrandDao carBrandDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CarBrandMapper carBrandMapper;
    @Autowired
    private CarModelMapper carModelMapper;
    @Autowired
    private CarModelDao carModelDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CarModelService carModelService;
    @Autowired
    private CarBrandService carBrandService;

    @GetMapping("/users")
    public List<UserResponse> getAllUsers(Principal principal) {
        System.err.println(principal);
        List<User> all = userService.findAll(0);

        return userMapper.toUserResponses(all);
    }
    @GetMapping("/my-page/{id}")
    public UserResponse getMyUser(@PathVariable long id) {
        return userMapper.toUserResponse(userDao.getOne(id));
    }

    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable long id) {
        return userMapper.toUserResponse(userDao.getOne(id));
    }

    @PostMapping("/users")
    public String register(@RequestBody @Valid UserCreateRequest userCreateRequest,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "ERRORRRR";
        }
        System.err.println(userCreateRequest);
        long id = userService.register(userMapper.toUser(userCreateRequest));
        return String.valueOf(id);
    }

    @PutMapping("/users")
    public UserResponse update(@RequestBody @Valid UserRequest userRequest,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.toString());
        }
        System.err.println(userRequest);
        return userMapper.toUserResponse(
                userService.update(
                        userMapper.toUser(userRequest)));
    }

    @GetMapping("/cars")
    public List<CarResponse> getAllCars() {
        return carMapper.toCarResponses(carDao.findAll());
    }

    @GetMapping("/brands")
    public List<CarBrandResponse> getAllBrands() {
        return carBrandMapper.toCarBrandResponses(carBrandDao.findAll());
    }

    @PutMapping("/brands")
    public CarBrandResponse update(@Valid @RequestBody CarBrandRequest carBrandRequest,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.toString());
        }

        return carBrandMapper.toCarBrandResponse(
                carBrandService.update(
                        carBrandMapper.toCarBrand(carBrandRequest)));
    }

    @GetMapping("/models")
    public List<CarModelResponse> getAllModels() {
        return carModelMapper.toCarModelResponses(carModelDao.findAll());
    }

    @PutMapping("/models")
    public CarModelResponse update(@Valid @RequestBody CarModelRequest carModelRequest,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.toString());
        }
        return carModelMapper.toCarModelResponse(
                carModelService.update(
                        carModelMapper.toCarModel(carModelRequest)));
    }

    @GetMapping("/admin")
    public String admin() {
        return "hi admin";
    }

    @GetMapping("/index")
    public String index() {
        return "hello guest";
    }

/*    @GetMapping("/me")
    public String forUser() {
        return "hello me";
    }*/
}
