package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
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
@RequestMapping("/api/models")
public class ModelController {

    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Create model
     */
    @SecuredForAdmin
    @PostMapping()
    public String create(@RequestBody @Valid ModelCreateRequest modelCreateRequest) {
        long id = modelService.create(modelMapper.toModel(modelCreateRequest));
        return "Success, model id = " + id;
    }

    /**
     * Get model by id
     */
    @GetMapping("/{modelId}")
    public ModelResponse getById(@PathVariable long modelId) {
        ModelEntity modelEntity = modelService.findById(modelId);
        return modelMapper.toModelResponse(modelEntity);
    }

    /**
     * Get all models
     */
    @GetMapping("/page/{pageNumber}")
    public List<ModelResponse> getAllActive(@PathVariable int pageNumber) {
        return modelMapper.toModelResponses(modelService.findAllActive(pageNumber));
    }

    /**
     * Get models by brand id
     */
    @GetMapping("/get-by-brand/{brandId}/page/{pageNumber}")
    public List<ModelResponse> getByBrand(@PathVariable long brandId, @PathVariable int pageNumber) {

        List<ModelEntity> modelEntity = modelService.findAllByBrand(brandId, pageNumber);
        return modelMapper.toModelResponses(modelEntity);
    }

    /**
     * Get all deleted models
     */
    @SecuredForAdmin
    @GetMapping("/deleted/page/{pageNumber}")
    public List<ModelResponse> getAllDeleted(@PathVariable int pageNumber) {

        List<ModelEntity> modelEntity = modelService.findAllDeleted(pageNumber);
        return modelMapper.toModelResponses(modelEntity);
    }

    /**
     * Search by keyword in model name
     */
    @PostMapping("/search")
    public List<ModelResponse> searchByPart(@RequestParam("keyword") String keyword) {
        return modelMapper.toModelResponses(modelService.findByKeyword(keyword));
    }

    /**
     * Update model
     */
    @SecuredForAdmin
    @PutMapping()
    public ModelResponse update(@RequestBody @Valid ModelUpdateRequest modelUpdateRequest) {
        ModelEntity updated = modelService.update(modelMapper.toModel(modelUpdateRequest));
        return modelMapper.toModelResponse(updated);
    }

    /**
     * Delete model
     */
    @SecuredForAdmin
    @DeleteMapping("/{modelId}")
    public void delete(@PathVariable long modelId) {
        modelService.deleteSoftById(modelId);
    }

    /**
     * Restore model from deleted
     */
    @SecuredForAdmin
    @PutMapping("/restore")
    public void restore(long id){
        modelService.restore(id);
    }
}
