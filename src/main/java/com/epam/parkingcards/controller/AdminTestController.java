//package com.epam.parkingcards.controller;
//
//import com.epam.parkingcards.controller.mapper.CarBrandMapper;
//import com.epam.parkingcards.controller.mapper.CarMapper;
//import com.epam.parkingcards.controller.mapper.CarModelMapper;
//import com.epam.parkingcards.controller.mapper.UserMapper;
//import com.epam.parkingcards.controller.request.*;
//import com.epam.parkingcards.controller.response.CarBrandResponse;
//import com.epam.parkingcards.controller.response.CarModelResponse;
//import com.epam.parkingcards.controller.response.CarResponse;
//import com.epam.parkingcards.controller.response.UserResponse;
//import com.epam.parkingcards.model.Car;
//import com.epam.parkingcards.model.CarBrand;
//import com.epam.parkingcards.model.CarModel;
//import com.epam.parkingcards.model.User;
//import com.epam.parkingcards.service.CarBrandService;
//import com.epam.parkingcards.service.CarModelService;
//import com.epam.parkingcards.service.CarService;
//import com.epam.parkingcards.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//public class AdminTestController {
//
//    @RestController("/admin")
//    public class UserController {
//
//        @Autowired
//        private UserService userService;
//        @Autowired
//        private UserMapper userMapper;
//
//        /**
//         * Get all users
//         */
//        @GetMapping("/users/page/{pageNumber}")
//        public List<UserResponse> getAllUsers(@PathVariable int pageNumber) {
//            return userMapper.toUserResponses(userService.findAll(pageNumber));
//        }
//
//        /**
//         * Get user by id
//         */
//        @GetMapping("/users/{userId}")
//        public UserResponse getUserById(@PathVariable long userId) {
//            User user = userService.findById(userId);
//            return userMapper.toUserResponse(user);
//        }
//
//        /**
//         * Get user by plate
//         */
//        @GetMapping("/users/by-plate/{plate}")
//        public UserResponse getByLicensePlate(@PathVariable String plate) {
//            User user = userService.findByLicensePlate(plate);
//            return userMapper.toUserResponse(user);
//        }
//
//        /**
//         * Create user
//         */
//        @PostMapping("/users")
//        public String register(@RequestBody @Valid UserCreateRequest userCreateRequest) {
//            long id = userService.register(userMapper.toUser(userCreateRequest));
//            return String.valueOf(id);
//        }
//
//        /**
//         * Update user
//         */
//        @PutMapping("/users")
//        public UserResponse update(@RequestBody @Valid UserRequest userRequest) {
//            User updated = userService.update(userMapper.toUser(userRequest));
//            return userMapper.toUserResponse(updated);
//        }
//
//        /**
//         * Delete user by ID
//         */
//        @DeleteMapping("/users/{userId}")
//        public void deleteUserById(@PathVariable long userId) {
//            userService.deleteById(userId);
//        }
//    }
//
//    @RestController("/admin")
//    public class CarController {
//
//        @Autowired
//        private CarService carService;
//        @Autowired
//        private CarMapper carMapper;
//
//        /**
//         * Get all cars
//         */
//        @GetMapping("/cars/page/{pageNumber}")
//        public List<CarResponse> getAllCars(@Valid @PathVariable int pageNumber) {
//            return carMapper.toCarResponses(carService.findAll(pageNumber));
//        }
//
//        /**
//         * Get car by id
//         */
//        @GetMapping("/users/{carId}")
//        public CarResponse getUserById(@Valid @PathVariable long carId) {
//            Car car = carService.findById(carId);
//            return carMapper.toCarResponse(car);
//        }
//
//        /**
//         * Create car and add to user
//         */
//
//        //todo requests problem
//
//
//        /**
//         * Update car
//         */
//        @PutMapping("/cars")
//        public CarResponse update(@RequestBody @Valid CarRequest carRequest) {
//            Car updated = carService.update(carMapper.toCar(carRequest));
//            return carMapper.toCarResponse(updated);
//        }
//
//        /**
//         * Delete car, and remove from user
//         */
//        @DeleteMapping("/cars/{carId}")
//        public void deleteCarFromUser(@PathVariable long carId) {
//            carService.deleteById(carId);
//        }
//    }
//
//    @RestController("/admin")
//    public class ModelController {
//
//        @Autowired
//        private CarModelService carModelService;
//        @Autowired
//        private CarModelMapper carModelMapper;
//
//        /**
//         * Get all models
//         */
//        @GetMapping("/models/page/{pageNumber}")
//        public List<CarModelResponse> getAllModels(@PathVariable int pageNumber) {
//            return carModelMapper.toCarModelResponses(carModelService.findAll(pageNumber));
//        }
//
//        /**
//         * Get model by id
//         */
//        @GetMapping("/models/{modelId}")
//        public CarModelResponse getModelById(@PathVariable long modelId) {
//            CarModel carModel = carModelService.findById(modelId);
//            return carModelMapper.toCarModelResponse(carModel);
//        }
//
//        /**
//         * Get model by brand id
//         */
//        @GetMapping("/models/{brandId}")
//        public List<CarModelResponse> getModelByBrandId(@PathVariable long brandId) {
//            List<CarModel> carModel = carModelService.findAllByBrand(brandId);
//            return carModelMapper.toCarModelResponses(carModel);
//        }
//
//        /**
//         * Create model
//         */
//        @PostMapping("/models")
//        public String register(@RequestBody @Valid CarModelRequest carModelRequest) {
//            long id = carModelService.create(carModelMapper.toCarModel(carModelRequest));
//            return String.valueOf(id);
//        }
//
//        /**
//         * Update car model
//         */
//        @PutMapping("/models")
//        public CarModelResponse update(@RequestBody @Valid CarModelRequest carModelRequest) {
//            CarModel updated = carModelService.update(carModelMapper.toCarModel(carModelRequest));
//            return carModelMapper.toCarModelResponse(updated);
//        }
//
//        /**
//         * Delete car model
//         */
//        @DeleteMapping("/cars/{modelId}")
//        public void deleteCarModel(@PathVariable long modelId) {
//            carModelService.deleteSoftById(modelId);
//        }
//    }
//
//    @RestController("/admin")
//    public class BrandController {
//
//        @Autowired
//        private CarBrandService carBrandService;
//        @Autowired
//        private CarBrandMapper carBrandMapper;
//
//        /**
//         * Get all brands
//         */
//        @GetMapping("/brands/page/{pageNumber}")
//        public List<CarBrandResponse> getAllBrands(@PathVariable int pageNumber) {
//            return carBrandMapper.toCarBrandResponses(carBrandService.findAll(pageNumber));
//        }
//
//        /**
//         * Get brand by id
//         */
//        @GetMapping("/brands/{brandId}")
//        public CarBrandResponse getBrandById(@PathVariable long brandId) {
//            CarBrand carBrand = carBrandService.findById(brandId);
//            return carBrandMapper.toCarBrandResponse(carBrand);
//        }
//
//        /**
//         * Create brand
//         */
//        @PostMapping("/brands")
//        public String register(@RequestBody @Valid CarBrandRequest carBrandRequest) {
//            long id = carBrandService.create(carBrandMapper.toCarBrand(carBrandRequest));
//            return String.valueOf(id);
//        }
//
//        /**
//         * Update car brand
//         */
//        @PutMapping("/brands")
//        public CarBrandResponse update(@RequestBody @Valid CarBrandRequest carBrandRequest) {
//            CarBrand updated = carBrandService.update(carBrandMapper.toCarBrand(carBrandRequest));
//            return carBrandMapper.toCarBrandResponse(updated);
//        }
//
//        /**
//         * Delete car model
//         */
//        @DeleteMapping("/brands/{brandId}")
//        public void deleteCarBrand(@PathVariable long brandId) {
//            carBrandService.deleteSoftById(brandId);
//        }
//    }
//}