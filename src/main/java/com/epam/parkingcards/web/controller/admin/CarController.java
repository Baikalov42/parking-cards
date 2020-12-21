package com.epam.parkingcards.web.controller.admin;

import com.epam.parkingcards.web.mapper.CarMapper;
import com.epam.parkingcards.web.request.admin.CarCreateRequest;
import com.epam.parkingcards.web.request.admin.CarUpdateRequest;
import com.epam.parkingcards.web.response.CarResponse;
import com.epam.parkingcards.model.CarEntity;
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
     * Create car and add to user
     */
    @PostMapping
    public String create(@RequestBody @Valid CarCreateRequest carCreateRequest) {
        long id = carService.create(carMapper.toCar(carCreateRequest));
        return "Car is registered, id = " + id;
    }

    /**
     * Get car by id
     */
    @GetMapping("/{carId}")
    public CarResponse getById(@Valid @PathVariable long carId) {
        CarEntity carEntity = carService.findById(carId);
        return carMapper.toCarResponse(carEntity);
    }

    /**
     * Get all cars
     */
    @GetMapping("/page/{pageNumber}")
    public List<CarResponse> getAll(@Valid @PathVariable int pageNumber) {
        return carMapper.toCarResponses(carService.findAll(pageNumber));
    }

    /**
     * Update car
     */
    @PutMapping
    public CarResponse update(@RequestBody @Valid CarUpdateRequest carUpdateRequest) {
        CarEntity updated = carService.update(carMapper.toCar(carUpdateRequest));
        return carMapper.toCarResponse(updated);
    }

    /**
     * Delete car, and remove from user
     */
    @DeleteMapping("/{carId}")
    public void delete(@PathVariable long carId) {
        carService.deleteById(carId);
    }
}