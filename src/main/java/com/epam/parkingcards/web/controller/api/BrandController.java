package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.web.mapper.BrandMapper;
import com.epam.parkingcards.web.request.admin.BrandCreateRequest;
import com.epam.parkingcards.web.request.admin.BrandUpdateRequest;
import com.epam.parkingcards.web.response.BrandResponse;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    @Secured("ROLE_admin")
    @PostMapping()
    public String create(@RequestBody @Valid BrandCreateRequest brandCreateRequest) {
        long id = brandService.create(brandMapper.toBrand(brandCreateRequest));
        return "Success, brand id = " + id;
    }

    /**
     * Get brand by id
     */
    @Secured({"ROLE_admin", "ROLE_user"})
    @GetMapping("/{brandId}")
    public BrandResponse getById(@PathVariable long brandId) {
        BrandEntity brandEntity = brandService.findById(brandId);
        return brandMapper.toBrandResponse(brandEntity);
    }

    /**
     * Get all brands
     */
    @Secured({"ROLE_admin", "ROLE_user"})
    @GetMapping("/page/{pageNumber}")
    public List<BrandResponse> getAll(@PathVariable int pageNumber) {
        return brandMapper.toBrandResponses(brandService.findAll(pageNumber));
    }

    /**
     * Get all deleted brands
     */
    @Secured("ROLE_admin")
    @GetMapping("/deleted/page/{pageNumber}")
    public List<BrandResponse> getAllDeleted(@PathVariable int pageNumber) {
        return brandMapper.toBrandResponses(brandService.findAllDeleted(pageNumber));
    }

    /**
     * Search by keyword in brand name
     */
    @Secured("ROLE_admin")
    @PostMapping("/search")
    public List<BrandResponse> searchByPart(@RequestParam("keyword") String keyword) {
        return brandMapper.toBrandResponses(brandService.findByKeyword(keyword));
    }

    /**
     * Update brand
     */
    @Secured("ROLE_admin")
    @PutMapping()
    public BrandResponse update(@RequestBody @Valid BrandUpdateRequest brandUpdateRequest) {
        BrandEntity updated = brandService.update(brandMapper.toBrand(brandUpdateRequest));
        return brandMapper.toBrandResponse(updated);
    }

    /**
     * Restore brand from deleted
     */
    @Secured("ROLE_admin")
    @PutMapping("/restore")
    public void restore(long id){
        brandService.restore(id);
    }

    /**
     * Delete brand
     */
    @Secured("ROLE_admin")
    @DeleteMapping("/{brandId}")
    public void delete(@PathVariable long brandId) {
        brandService.deleteSoftById(brandId);
    }
}