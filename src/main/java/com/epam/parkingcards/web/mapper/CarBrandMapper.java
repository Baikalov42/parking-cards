package com.epam.parkingcards.web.mapper;

import com.epam.parkingcards.web.request.admin.BrandCreateRequest;
import com.epam.parkingcards.web.request.admin.BrandUpdateRequest;
import com.epam.parkingcards.web.response.CarBrandResponse;
import com.epam.parkingcards.model.CarBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarBrandMapper {

    public CarBrand toCarBrand(BrandUpdateRequest brandUpdateRequest) {

        CarBrand carBrand = new CarBrand();
        carBrand.setId(brandUpdateRequest.getId());
        carBrand.setName(brandUpdateRequest.getName());

        return carBrand;
    }

    public CarBrand toCarBrand(BrandCreateRequest brandCreateRequest) {

        CarBrand carBrand = new CarBrand();
        carBrand.setName(brandCreateRequest.getName());

        return carBrand;
    }

    public CarBrandResponse toCarBrandResponse(CarBrand carBrand) {
        CarBrandResponse carBrandResponse = new CarBrandResponse();

        carBrandResponse.setId(carBrand.getId());
        carBrandResponse.setName(carBrand.getName());

        return carBrandResponse;
    }

    public List<CarBrandResponse> toCarBrandResponses(List<CarBrand> carBrands) {

        List<CarBrandResponse> carBrandResponses = new ArrayList<>();
        for (CarBrand carBrand : carBrands) {
            carBrandResponses.add(toCarBrandResponse(carBrand));
        }
        return carBrandResponses;
    }
}
