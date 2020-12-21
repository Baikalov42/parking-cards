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
     * Get all brands
     */
    @GetMapping("/page/{pageNumber}")
    public List<BrandResponse> getAllBrands(@PathVariable int pageNumber) {
        return brandMapper.toCarBrandResponses(brandService.findAll(pageNumber));
    }

    /**
     * Get brand by id
     */
    @GetMapping("/{brandId}")
    public BrandResponse getBrandById(@PathVariable long brandId) {
        BrandEntity brandEntity = brandService.findById(brandId);
        return brandMapper.toCarBrandResponse(brandEntity);
    }

    /**
     * Create brand
     */
    @PostMapping()
    public String register(@RequestBody @Valid BrandCreateRequest brandCreateRequest) {
        long id = brandService.create(brandMapper.toCarBrand(brandCreateRequest));
        return String.valueOf(id);
    }

    /**
     * Update car brand
     */
    @PutMapping()
    public BrandResponse update(@RequestBody @Valid BrandUpdateRequest brandUpdateRequest) {
        BrandEntity updated = brandService.update(brandMapper.toCarBrand(brandUpdateRequest));
        return brandMapper.toCarBrandResponse(updated);
    }

    /**
     * Delete car model
     */
    @DeleteMapping("/{brandId}")
    public void deleteCarBrand(@PathVariable long brandId) {
        brandService.deleteSoftById(brandId);
    }
}