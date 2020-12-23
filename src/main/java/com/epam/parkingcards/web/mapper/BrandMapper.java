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

    public BrandEntity toBrand(BrandUpdateRequest brandUpdateRequest) {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(brandUpdateRequest.getId());
        brandEntity.setName(brandUpdateRequest.getName());

        return brandEntity;
    }

    public BrandEntity toBrand(BrandCreateRequest brandCreateRequest) {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName(brandCreateRequest.getName());

        return brandEntity;
    }

    public BrandResponse toBrandResponse(BrandEntity brandEntity) {
        BrandResponse brandResponse = new BrandResponse();

        brandResponse.setId(brandEntity.getId());
        brandResponse.setName(brandEntity.getName());
        brandResponse.setDeleted(brandEntity.isDeleted());

        return brandResponse;
    }

    public List<BrandResponse> toBrandResponses(List<BrandEntity> brandEntities) {

        List<BrandResponse> brandResponses = new ArrayList<>();
        for (BrandEntity brandEntity : brandEntities) {
            brandResponses.add(toBrandResponse(brandEntity));
        }
        return brandResponses;
    }
}
