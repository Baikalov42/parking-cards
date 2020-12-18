package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarModelDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.CarModel;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CarModelService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CarModelDao carModelDao;
    @Autowired
    private CarBrandService carBrandService;
    @Autowired
    private IdValidator idValidator;

    public long create(CarModel carModel) {
        try {
            return carModelDao.save(carModel).getId();
        } catch (DataAccessException e) {
            throw new DaoException("Creation error, model: " + carModel, e);
        }
    }

    public CarModel findById(long id) {
        idValidator.validate(id);

        return carModelDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, Model not found", id)));
    }

    public Collection<CarModel> findAll(int pageNumber) {
        //TODO: filtering find all
        return null;
    }

    public List<CarModel> findAllByBrand(long brandId, int pageNumber) {
        //TODO: filtering find all
        return null;
    }

    public CarModel update(CarModel carModel) {

        idValidator.validate(
                carModel.getId(),
                carModel.getCarBrand().getId());

        validateForExistence(carModel.getId());
        carBrandService.validateForExistence(carModel
                .getCarBrand()
                .getId());

        try {
            return carModelDao.saveAndFlush(carModel);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, Model: %s", carModel), e);
        }
    }

    public void deleteSoftById(long id) {
        idValidator.validate(id);
        try {
            CarModel carModel = carModelDao.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("By id %d, Model not found", id)));

            carModel.setDeleted(true);
            carModelDao.saveAndFlush(carModel);
            //TODO: is ok ?
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: id=%d ", id), e);
        }
    }

    public void validateForExistence(long id) {
        if (!carModelDao.existsById(id)) {
            throw new ValidationException(String.format("Not exist, id=%d", id));
        }
    }
}
