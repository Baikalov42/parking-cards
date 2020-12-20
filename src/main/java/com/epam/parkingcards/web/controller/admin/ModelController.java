package com.epam.parkingcards.web.controller.admin;

import com.epam.parkingcards.web.mapper.CarModelMapper;
import com.epam.parkingcards.web.request.admin.ModelUpdateRequest;
import com.epam.parkingcards.web.response.CarModelResponse;
import com.epam.parkingcards.model.CarModel;
import com.epam.parkingcards.service.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/models")
public class ModelController {

    @Autowired
    private CarModelService carModelService;
    @Autowired
    private CarModelMapper carModelMapper;

    /**
     * Get all models
     */
    @GetMapping("/page/{pageNumber}")
    public List<CarModelResponse> getAllModels(@PathVariable int pageNumber) {
        return carModelMapper.toCarModelResponses(carModelService.findAll(pageNumber));
    }

    /**
     * Get model by id
     */
    @GetMapping("/{modelId}")
    public CarModelResponse getModelById(@PathVariable long modelId) {
        CarModel carModel = carModelService.findById(modelId);
        return carModelMapper.toCarModelResponse(carModel);
    }

    /**
     * Get model by brand id
     */
    @GetMapping("/{brandId}")
    public List<CarModelResponse> getModelByBrandId(@PathVariable long brandId) {
        List<CarModel> carModel = carModelService.findAllByBrand(brandId);
        return carModelMapper.toCarModelResponses(carModel);
    }

    /**
     * Create model
     */
    @PostMapping()
    public String register(@RequestBody @Valid ModelUpdateRequest modelUpdateRequest) {
        long id = carModelService.create(carModelMapper.toCarModel(modelUpdateRequest));
        return String.valueOf(id);
    }

    /**
     * Update car model
     */
    @PutMapping()
    public CarModelResponse update(@RequestBody @Valid ModelUpdateRequest modelUpdateRequest) {
        CarModel updated = carModelService.update(carModelMapper.toCarModel(modelUpdateRequest));
        return carModelMapper.toCarModelResponse(updated);
    }

    /**
     * Delete car model
     */
    @DeleteMapping("/{modelId}")
    public void deleteCarModel(@PathVariable long modelId) {
        carModelService.deleteSoftById(modelId);
    }
}
