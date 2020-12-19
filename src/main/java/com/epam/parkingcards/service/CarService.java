package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.Car;
import com.epam.parkingcards.model.User;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CarDao carDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CarModelService carModelService;
    @Autowired
    private IdValidator idValidator;

    public long create(Car car) {

        try {
            return carDao.saveAndFlush(car).getId();
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Creation error, Car: %s ", car), e);
        }
    }

    public Car findById(long id) {
        idValidator.validate(id);

        return carDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, Car not found", id)));
    }

    public List<Car> findAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Car> result = carDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    //TODO implement updateCarIgnoreUserId
    public Car update(Car car) {
        idValidator.validate(car.getId());

        this.validateForExistence(car.getId());
        userService.validateForExistence(car
                .getUser()
                .getId());
        carModelService.validateForExistence(car
                .getCarModel()
                .getId());

        try {
            return carDao.saveAndFlush(car);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, Car: %s", car), e);
        }
    }

    @Transactional
    public void deleteById(long id) {

        idValidator.validate(id);
        validateForExistence(id);

        try {
            User user = userService.findByCarId(id);
            user.getCars().removeIf(x -> x.getId() == id);
            carDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: id=%d ", id), e);
        }
    }

    public void validateForExistence(long id) {
        if (!carDao.existsById(id)) {
            throw new ValidationException(String.format("Not exist, id=%d", id));
        }
    }
}
