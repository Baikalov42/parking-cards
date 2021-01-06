package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.web.mapper.BrandMapper;
import com.epam.parkingcards.web.request.BrandCreateRequest;
import com.epam.parkingcards.web.request.BrandUpdateRequest;
import com.epam.parkingcards.web.response.BrandResponse;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandMapper brandMapper;

    /**
     * Create brand
     */
    @SecuredForAdmin
    @PostMapping()
    public ResponseEntity<String> create(@RequestBody @Valid BrandCreateRequest brandCreateRequest) {

        long id = brandService.create(brandMapper.toBrand(brandCreateRequest));
        return new ResponseEntity<>("Success, new brand id = " + id, HttpStatus.OK);
    }

    /**
     * Get brand by id
     */

    @GetMapping("/{brandId}")
    public BrandResponse getById(@PathVariable long brandId) {
        BrandEntity brandEntity = brandService.findById(brandId);
        return brandMapper.toBrandResponse(brandEntity);
    }

    /**
     * Get all brands
     */
    @GetMapping("/page/{pageNumber}")
    public List<BrandResponse> getAllActive(@PathVariable int pageNumber) {
        return brandMapper.toBrandResponses(brandService.findAllActive(pageNumber));
    }

    /**
     * Get all deleted brands
     */
    @SecuredForAdmin
    @GetMapping("/deleted/page/{pageNumber}")
    public List<BrandResponse> getAllDeleted(@PathVariable int pageNumber) {
        return brandMapper.toBrandResponses(brandService.findAllDeleted(pageNumber));
    }

    /**
     * Search by keyword in brand name
     */
    @PostMapping("/search")
    public List<BrandResponse> searchByPart(@RequestParam("keyword") String keyword) {
        return brandMapper.toBrandResponses(brandService.findByKeyword(keyword));
    }

    /**
     * Update brand
     */
    @SecuredForAdmin
    @PutMapping()
    public BrandResponse update(@RequestBody @Valid BrandUpdateRequest brandUpdateRequest) {
        BrandEntity updated = brandService.update(brandMapper.toBrand(brandUpdateRequest));
        return brandMapper.toBrandResponse(updated);
    }

    /**
     * Restore brand from deleted
     */
    @SecuredForAdmin
    @PutMapping("/restore")
    public void restore(long id){
        brandService.restore(id);
    }

    /**
     * Delete brand
     */
    @SecuredForAdmin
    @DeleteMapping("/{brandId}")
    public void delete(@PathVariable long brandId) {
        brandService.deleteSoftById(brandId);
    }
}