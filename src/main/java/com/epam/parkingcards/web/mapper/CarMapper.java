package com.epam.parkingcards.web.mapper;

import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.web.request.admin.CarCreateRequest;
import com.epam.parkingcards.web.request.admin.CarUpdateRequest;
import com.epam.parkingcards.web.request.me.MeCarCreateRequest;
import com.epam.parkingcards.web.request.me.MeCarUpdateRequest;
import com.epam.parkingcards.web.response.CarResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarMapper {


    public CarEntity toCar(CarCreateRequest carCreateRequest) {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(carCreateRequest.getUserId());

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(carCreateRequest.getModelId());

        CarEntity carEntity = new CarEntity();
        carEntity.setLicensePlate(carCreateRequest.getLicensePlate());
        carEntity.setUserEntity(userEntity);
        carEntity.setModelEntity(modelEntity);

        return carEntity;
    }

    public CarEntity toCar(CarUpdateRequest carUpdateRequest) {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(carUpdateRequest.getUserId());

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(carUpdateRequest.getModelId());

        CarEntity carEntity = new CarEntity();

        carEntity.setId(carUpdateRequest.getId());
        carEntity.setLicensePlate(carUpdateRequest.getLicensePlate());
        carEntity.setUserEntity(userEntity);
        carEntity.setModelEntity(modelEntity);

        return carEntity;
    }

    public CarEntity toCar(MeCarUpdateRequest meCarUpdateRequest) {

        UserEntity userEntity = new UserEntity();

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(meCarUpdateRequest.getModelId());

        CarEntity carEntity = new CarEntity();
        carEntity.setId(meCarUpdateRequest.getId());
        carEntity.setLicensePlate(meCarUpdateRequest.getLicensePlate());
        carEntity.setUserEntity(userEntity);
        carEntity.setModelEntity(modelEntity);

        return carEntity;
    }

    public CarEntity toCar(MeCarCreateRequest meCarCreateRequest) {

        UserEntity userEntity = new UserEntity();

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(meCarCreateRequest.getModelId());

        CarEntity carEntity = new CarEntity();
        carEntity.setLicensePlate(meCarCreateRequest.getLicensePlate());
        carEntity.setUserEntity(userEntity);
        carEntity.setModelEntity(modelEntity);

        return carEntity;
    }

    public CarResponse toCarResponse(CarEntity carEntity) {

        CarResponse carResponse = new CarResponse();
        carResponse.setId(carEntity.getId());
        carResponse.setLicensePlate(carEntity.getLicensePlate());
        carResponse.setModelId(carEntity.getModelEntity().getId());

        return carResponse;
    }

    public List<CarResponse> toCarResponses(List<CarEntity> carEntities) {

        List<CarResponse> carResponses = new ArrayList<>();
        for (CarEntity carEntity : carEntities) {
            carResponses.add(toCarResponse(carEntity));
        }
        return carResponses;
    }
}
