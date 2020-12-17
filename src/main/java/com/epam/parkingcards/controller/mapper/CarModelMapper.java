package com.epam.parkingcards.controller.mapper;

import com.epam.parkingcards.controller.request.CarModelRequest;
import com.epam.parkingcards.controller.response.CarModelResponse;
import com.epam.parkingcards.model.CarBrand;
import com.epam.parkingcards.model.CarModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarModelMapper {

    @Autowired
    private CarBrandMapper carBrandMapper;

    public CarModel toCarModel(CarModelRequest carModelRequest) {
        CarBrand carBrand = new CarBrand();
        carBrand.setId(carModelRequest.getBranId());

        CarModel carModel = new CarModel();
        carModel.setId(carModelRequest.getId());
        carModel.setName(carModelRequest.getName());
        carModel.setCarBrand(carBrand);

        return carModel;
    }

    public CarModelResponse toCarModelResponse(CarModel carModel) {
        CarModelResponse carModelResponse = new CarModelResponse();

        carModelResponse.setId(carModel.getId());
        carModelResponse.setName(carModel.getName());
        carModelResponse.setBrand(carBrandMapper.toCarBrandResponse(carModel.getCarBrand()));

        return carModelResponse;
    }

    public List<CarModelResponse> toCarModelResponses(List<CarModel> carModels) {

        List<CarModelResponse> carModelResponses = new ArrayList<>();
        for (CarModel model : carModels) {
            carModelResponses.add(toCarModelResponse(model));
        }
        return carModelResponses;
    }
}
