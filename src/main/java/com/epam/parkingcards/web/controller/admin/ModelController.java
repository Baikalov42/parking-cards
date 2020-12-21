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
     * Create model
     */
    @PostMapping()
    public String create(@RequestBody @Valid ModelCreateRequest modelCreateRequest) {
        long id = modelService.create(modelMapper.toCarModel(modelCreateRequest));
        return "Success, model id = " + id;
    }

    /**
     * Get model by id
     */
    @GetMapping("/{modelId}")
    public ModelResponse getById(@PathVariable long modelId) {
        ModelEntity modelEntity = modelService.findById(modelId);
        return modelMapper.toCarModelResponse(modelEntity);
    }

    /**
     * Get all models
     */
    @GetMapping("/page/{pageNumber}")
    public List<ModelResponse> getAll(@PathVariable int pageNumber) {
        return modelMapper.toCarModelResponses(modelService.findAll(pageNumber));
    }

    /**
     * Get models by brand id
     */
    @GetMapping("/get-by-brand/{brandId}/page/{pageNumber}")
    public List<ModelResponse> getByBrand(@PathVariable long brandId, @PathVariable int pageNumber) {

        List<ModelEntity> modelEntity = modelService.findAllByBrand(brandId, pageNumber);
        return modelMapper.toCarModelResponses(modelEntity);
    }

    /**
     * Get all deleted models
     */
    @GetMapping("/deleted/page/{pageNumber}")
    public List<ModelResponse> getAllDeleted(@PathVariable int pageNumber) {

        List<ModelEntity> modelEntity = modelService.findAllDeleted(pageNumber);
        return modelMapper.toCarModelResponses(modelEntity);
    }

    /**
     * Update model
     */
    @PutMapping()
    public ModelResponse update(@RequestBody @Valid ModelUpdateRequest modelUpdateRequest) {
        ModelEntity updated = modelService.update(modelMapper.toCarModel(modelUpdateRequest));
        return modelMapper.toCarModelResponse(updated);
    }

    /**
     * Delete model
     */
    @DeleteMapping("/{modelId}")
    public void delete(@PathVariable long modelId) {
        modelService.deleteSoftById(modelId);
    }
}
