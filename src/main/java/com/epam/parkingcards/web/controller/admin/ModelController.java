package com.epam.parkingcards.web.controller.admin;

import com.epam.parkingcards.web.mapper.ModelMapper;
import com.epam.parkingcards.web.request.admin.ModelCreateRequest;
import com.epam.parkingcards.web.request.admin.ModelUpdateRequest;
import com.epam.parkingcards.web.response.ModelResponse;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/models")
public class ModelController {

    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all models
     */
    @GetMapping("/page/{pageNumber}")
    public List<ModelResponse> getAllModels(@PathVariable int pageNumber) {
        return modelMapper.toCarModelResponses(modelService.findAll(pageNumber));
    }

    /**
     * Get model by id
     */
    @GetMapping("/{modelId}")
    public ModelResponse getModelById(@PathVariable long modelId) {
        ModelEntity modelEntity = modelService.findById(modelId);
        return modelMapper.toCarModelResponse(modelEntity);
    }

    /**
     * Get model by brand id
     */
    @GetMapping("/get-by-brand/{brandId}")
    public List<ModelResponse> getModelByBrandId(@PathVariable long brandId) {
        List<ModelEntity> modelEntity = modelService.findAllByBrand(brandId);
        return modelMapper.toCarModelResponses(modelEntity);
    }

    /**
     * Create model
     */
    @PostMapping()
    public String register(@RequestBody @Valid ModelCreateRequest modelCreateRequest) {
        long id = modelService.create(modelMapper.toCarModel(modelCreateRequest));
        return String.valueOf(id);
    }

    /**
     * Update car model
     */
    @PutMapping()
    public ModelResponse update(@RequestBody @Valid ModelUpdateRequest modelUpdateRequest) {
        ModelEntity updated = modelService.update(modelMapper.toCarModel(modelUpdateRequest));
        return modelMapper.toCarModelResponse(updated);
    }

    /**
     * Delete car model
     */
    @DeleteMapping("/{modelId}")
    public void deleteCarModel(@PathVariable long modelId) {
        modelService.deleteSoftById(modelId);
    }
}
