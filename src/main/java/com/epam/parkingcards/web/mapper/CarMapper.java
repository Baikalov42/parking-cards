package com.epam.parkingcards.web.mapper;

import com.epam.parkingcards.dao.CarBrandDao;
import com.epam.parkingcards.dao.CarModelDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.web.request.admin.CarCreateRequest;
import com.epam.parkingcards.web.request.admin.CarUpdateRequest;
import com.epam.parkingcards.web.request.me.MeCarCreateRequest;
import com.epam.parkingcards.web.request.me.MeCarUpdateRequest;
import com.epam.parkingcards.web.response.CarResponse;
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
    private CarModelDao carModelDao;

    @Autowired
    private CarBrandDao carBrandDao;

    @Autowired
    UserDao userDao;

    @Autowired
    private CarModelMapper carModelMapper;

    public Car toCar(CarCreateRequest carCreateRequest) {

        User user = new User();
        user.setId(carCreateRequest.getUserId());

        CarModel carModel = new CarModel();
        carModel.setId(carCreateRequest.getModelId());

        Car car = new Car();
        car.setLicensePlate(carCreateRequest.getLicensePlate());
        car.setUser(user);
        car.setCarModel(carModel);

        return car;
    }

    public Car toCar(CarUpdateRequest carUpdateRequest) {

        User user = new User();
        user.setId(carUpdateRequest.getUserId());

        CarModel carModel = new CarModel();
        carModel.setId(carUpdateRequest.getModelId());

        Car car = new Car();
        car.setId(carUpdateRequest.getId());
        car.setLicensePlate(carUpdateRequest.getLicensePlate());
        car.setUser(userDao.getOne(carUpdateRequest.getUserId()));
        car.setCarModel(carModelDao.getOne(carUpdateRequest.getModelId()));

        return car;
    }

    public Car toCar(MeCarUpdateRequest meCarUpdateRequest) {

        User user = new User();

        CarModel carModel = new CarModel();
        carModel.setId(meCarUpdateRequest.getModelId());

        Car car = new Car();
        car.setId(meCarUpdateRequest.getId());
        car.setLicensePlate(meCarUpdateRequest.getLicensePlate());
        car.setUser(user);
        car.setCarModel(carModel);

        return car;
    }

    public Car toCar(MeCarCreateRequest meCarCreateRequest) {

        User user = new User();

        CarModel carModel = new CarModel();
        carModel.setId(meCarCreateRequest.getModelId());

        Car car = new Car();
        car.setLicensePlate(meCarCreateRequest.getLicensePlate());
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
