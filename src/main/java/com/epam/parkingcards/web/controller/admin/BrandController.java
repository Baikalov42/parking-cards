package com.epam.parkingcards.web.controller.admin;

import com.epam.parkingcards.web.mapper.CarBrandMapper;
import com.epam.parkingcards.web.request.admin.BrandCreateRequest;
import com.epam.parkingcards.web.request.admin.BrandUpdateRequest;
import com.epam.parkingcards.web.response.CarBrandResponse;
import com.epam.parkingcards.model.CarBrand;
import com.epam.parkingcards.service.CarBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/brands")
public class BrandController {

    @Autowired
    private CarBrandService carBrandService;
    @Autowired
    private CarBrandMapper carBrandMapper;

    /**
     * Get all brands
     */
    @GetMapping("/page/{pageNumber}")
    public List<CarBrandResponse> getAllBrands(@PathVariable int pageNumber) {
        return carBrandMapper.toCarBrandResponses(carBrandService.findAll(pageNumber));
    }

    /**
     * Get brand by id
     */
    @GetMapping("/{brandId}")
    public CarBrandResponse getBrandById(@PathVariable long brandId) {
        CarBrand carBrand = carBrandService.findById(brandId);
        return carBrandMapper.toCarBrandResponse(carBrand);
    }

    /**
     * Create brand
     */
    @PostMapping()
    public String register(@RequestBody @Valid BrandCreateRequest brandCreateRequest) {
        long id = carBrandService.create(carBrandMapper.toCarBrand(brandCreateRequest));
        return String.valueOf(id);
    }

    /**
     * Update car brand
     */
    @PutMapping()
    public CarBrandResponse update(@RequestBody @Valid BrandUpdateRequest brandUpdateRequest) {
        CarBrand updated = carBrandService.update(carBrandMapper.toCarBrand(brandUpdateRequest));
        return carBrandMapper.toCarBrandResponse(updated);
    }

    /**
     * Delete car model
     */
    @DeleteMapping("/{brandId}")
    public void deleteCarBrand(@PathVariable long brandId) {
        carBrandService.deleteSoftById(brandId);
    }
}