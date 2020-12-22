package com.epam.parkingcards.web.controller.admin;

import com.epam.parkingcards.web.mapper.BrandMapper;
import com.epam.parkingcards.web.request.admin.BrandCreateRequest;
import com.epam.parkingcards.web.request.admin.BrandUpdateRequest;
import com.epam.parkingcards.web.response.BrandResponse;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandMapper brandMapper;

    /**
     * Create brand
     */
    @PostMapping()
    public String create(@RequestBody @Valid BrandCreateRequest brandCreateRequest) {
        long id = brandService.create(brandMapper.toCarBrand(brandCreateRequest));
        return "Success, brand id = " + id;
    }

    /**
     * Get brand by id
     */
    @GetMapping("/{brandId}")
    public BrandResponse getById(@PathVariable long brandId) {
        BrandEntity brandEntity = brandService.findById(brandId);
        return brandMapper.toCarBrandResponse(brandEntity);
    }

    /**
     * Get all brands
     */
    @GetMapping("/page/{pageNumber}")
    public List<BrandResponse> getAll(@PathVariable int pageNumber) {
        return brandMapper.toCarBrandResponses(brandService.findAll(pageNumber));
    }

    /**
     * Get all deleted brands
     */
    @GetMapping("/deleted/page/{pageNumber}")
    public List<BrandResponse> getAllDeleted(@PathVariable int pageNumber) {
        return brandMapper.toCarBrandResponses(brandService.findAllDeleted(pageNumber));
    }

    /**
     * Update brand
     */
    @PutMapping()
    public BrandResponse update(@RequestBody @Valid BrandUpdateRequest brandUpdateRequest) {
        BrandEntity updated = brandService.update(brandMapper.toCarBrand(brandUpdateRequest));
        return brandMapper.toCarBrandResponse(updated);
    }

    /**
     * Delete brand
     */
    @DeleteMapping("/{brandId}")
    public void delete(@PathVariable long brandId) {
        brandService.deleteSoftById(brandId);
    }
}