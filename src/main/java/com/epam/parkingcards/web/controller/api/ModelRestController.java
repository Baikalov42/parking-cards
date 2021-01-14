package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.web.mapper.ModelMapper;
import com.epam.parkingcards.web.request.ModelCreateRequest;
import com.epam.parkingcards.web.request.ModelUpdateRequest;
import com.epam.parkingcards.web.response.ModelResponse;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/models")
public class ModelRestController {

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
        return "Success, new model id = " + id;
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
        return modelMapper.toModelResponses(modelService.findAllActive(pageNumber).getContent());
    }

    /**
     * Get models by brand id
     */
    @GetMapping("/get-by-brand/{brandId}/page/{pageNumber}")
    public List<ModelResponse> getByBrand(@PathVariable long brandId, @PathVariable int pageNumber) {

        List<ModelEntity> modelEntity = modelService.findAllByBrand(brandId, pageNumber);
        return modelMapper.toModelResponses(modelEntity);
    }

    @GetMapping("/get-by-brand/{brandId}")
    public Map<Long, String> getModelsMapByBrandId(@PathVariable long brandId) {
        return modelService.getModelsMapByBrandId(brandId);
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
    @PutMapping("/restore/{modelId}")
    public void restore(@PathVariable long modelId) {
        modelService.restore(modelId);
    }
}
