package com.epam.parkingcards.web.mapper;

import com.epam.parkingcards.web.request.admin.ModelCreateRequest;
import com.epam.parkingcards.web.request.admin.ModelUpdateRequest;
import com.epam.parkingcards.web.response.CarModelResponse;
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

    public CarModel toCarModel(ModelUpdateRequest modelUpdateRequest) {
        CarBrand carBrand = new CarBrand();
        carBrand.setId(modelUpdateRequest.getBrandId());

        CarModel carModel = new CarModel();
        carModel.setId(modelUpdateRequest.getId());
        carModel.setName(modelUpdateRequest.getName());
        carModel.setCarBrand(carBrand);

        return carModel;
    }

    public CarModel toCarModel(ModelCreateRequest modelCreateRequest) {

        CarBrand carBrand = new CarBrand();
        carBrand.setId(modelCreateRequest.getBrandId());

        CarModel carModel = new CarModel();

        carModel.setName(modelCreateRequest.getName());
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
