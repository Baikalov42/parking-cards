package com.epam.parkingcards.controller.mapper;

import com.epam.parkingcards.controller.request.CarRequest;
import com.epam.parkingcards.controller.response.CarResponse;
import com.epam.parkingcards.model.Car;
import com.epam.parkingcards.model.CarModel;
import com.epam.parkingcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarModelMapper carModelMapper;

    public Car toCar(CarRequest carRequest) {

        User user = new User();
        user.setId(carRequest.getUserId());

        CarModel carModel = new CarModel();
        carModel.setId(carRequest.getModelId());

        Car car = new Car();
        car.setId(carRequest.getId());
        car.setLicensePlate(carRequest.getLicensePlate());
        car.setUser(user);
        car.setCarModel(carModel);

        return car;
    }

    public CarResponse toCarResponse(Car car) {

        CarResponse carResponse = new CarResponse();
        carResponse.setId(car.getId());
        carResponse.setLicensePlate(car.getLicensePlate());
        carResponse.setModel(carModelMapper.toCarModelResponse(car.getCarModel()));

        return carResponse;
    }

    public List<CarResponse> toCarResponses(List<Car> cars) {

        List<CarResponse> carResponses = new ArrayList<>();
        for (Car car : cars) {
            carResponses.add(toCarResponse(car));
        }
        return carResponses;
    }
}
