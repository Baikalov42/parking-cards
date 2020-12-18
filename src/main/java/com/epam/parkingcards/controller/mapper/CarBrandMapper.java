package com.epam.parkingcards.controller.mapper;

import com.epam.parkingcards.controller.request.CarBrandRequest;
import com.epam.parkingcards.controller.response.CarBrandResponse;
import com.epam.parkingcards.model.CarBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarBrandMapper {

    @Autowired
    private CarModelMapper carModelMapper;

    public CarBrand toCarBrand(CarBrandRequest carBrandRequest) {

        CarBrand carBrand = new CarBrand();
        carBrand.setId(carBrandRequest.getId());
        carBrand.setName(carBrandRequest.getName());

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
