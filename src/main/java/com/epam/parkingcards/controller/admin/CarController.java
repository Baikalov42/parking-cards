package com.epam.parkingcards.controller.admin;

import com.epam.parkingcards.controller.mapper.CarMapper;
import com.epam.parkingcards.controller.request.CarRequest;
import com.epam.parkingcards.controller.response.CarResponse;
import com.epam.parkingcards.model.Car;
import com.epam.parkingcards.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/cars")
public class CarController {

    @Autowired
    private CarService carService;
    @Autowired
    private CarMapper carMapper;

    /**
     * Get all cars
     */
    @GetMapping("/page/{pageNumber}")
    public List<CarResponse> getAllCars(@Valid @PathVariable int pageNumber) {
        return carMapper.toCarResponses(carService.findAll(pageNumber));
    }

    /**
     * Get car by id
     */
    @GetMapping("/{carId}")
    public CarResponse getUserById(@Valid @PathVariable long carId) {
        Car car = carService.findById(carId);
        return carMapper.toCarResponse(car);
    }

    /**
     * Create car and add to user
     */

    //todo requests problem


    /**
     * Update car
     */
    @PutMapping()
    public CarResponse update(@RequestBody @Valid CarRequest carRequest) {
        Car updated = carService.update(carMapper.toCar(carRequest));
        return carMapper.toCarResponse(updated);
    }

    /**
     * Delete car, and remove from user
     */
    @DeleteMapping("/{carId}")
    public void deleteCarFromUser(@PathVariable long carId) {
        carService.deleteById(carId);
    }
}