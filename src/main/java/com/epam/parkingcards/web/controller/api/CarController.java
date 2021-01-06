package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.web.mapper.CarMapper;
import com.epam.parkingcards.web.request.CarCreateRequest;
import com.epam.parkingcards.web.request.CarUpdateRequest;
import com.epam.parkingcards.web.response.CarResponse;
import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;
    @Autowired
    private CarMapper carMapper;

    /**
     * Create car and add to user
     */
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CarCreateRequest carCreateRequest) {
        long id = carService.create(carMapper.toCar(carCreateRequest));
        return new ResponseEntity<>("Success, new car id = " + id, HttpStatus.OK);
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
    @SecuredForAdmin
    @GetMapping("/page/{pageNumber}")
    public List<CarResponse> getAll(@Valid @PathVariable int pageNumber) {
        return carMapper.toCarResponses(carService.findAll(pageNumber));
    }

    /**
     * Get users cars
     */
    @GetMapping("/by-user-id/{userId}")
    public List<CarResponse> getByUserId(@PathVariable long userId) {
        return carMapper.toCarResponses(carService.findByUserId(userId));
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