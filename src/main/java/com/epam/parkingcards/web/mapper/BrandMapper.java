package com.epam.parkingcards.web.mapper;

import com.epam.parkingcards.web.request.admin.BrandCreateRequest;
import com.epam.parkingcards.web.request.admin.BrandUpdateRequest;
import com.epam.parkingcards.web.response.BrandResponse;
import com.epam.parkingcards.model.BrandEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandMapper {

    public BrandEntity toCarBrand(BrandUpdateRequest brandUpdateRequest) {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(brandUpdateRequest.getId());
        brandEntity.setName(brandUpdateRequest.getName());

        return brandEntity;
    }

    public BrandEntity toCarBrand(BrandCreateRequest brandCreateRequest) {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName(brandCreateRequest.getName());

        return brandEntity;
    }

    public BrandResponse toCarBrandResponse(BrandEntity brandEntity) {
        BrandResponse brandResponse = new BrandResponse();

        brandResponse.setId(brandEntity.getId());
        brandResponse.setName(brandEntity.getName());

        return brandResponse;
    }

    public List<BrandResponse> toCarBrandResponses(List<BrandEntity> brandEntities) {

        List<BrandResponse> brandRespons = new ArrayList<>();
        for (BrandEntity brandEntity : brandEntities) {
            brandRespons.add(toCarBrandResponse(brandEntity));
        }
        return brandRespons;
    }
}
