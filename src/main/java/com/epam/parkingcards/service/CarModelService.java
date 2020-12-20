package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarModelDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.CarModel;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //TODO: check if there is soft deleted model with the same name
    //TODO: if found, set deleted to false
    public long create(CarModel carModel) {
        try {
            return carModelDao.saveAndFlush(carModel).getId();
        } catch (DataAccessException e) {
            throw new DaoException("Creation error, model: " + carModel, e);
        }
    }

    public CarModel findById(long id) {
        idValidator.validate(id);

        return carModelDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, Model not found", id)));
    }

    public List<CarModel> findAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<CarModel> result = carModelDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    //TODO: check changed logic - whe retrieve models from CarModelDao, this way we will get only not deleted items
    public List<CarModel> findAllByBrand(long brandId) {
        return new ArrayList<>(carModelDao.findByCarBrand(carBrandService.findById(brandId)));
    }

    //TODO: Think about situation when setting the same name as soft-deleted one
    public CarModel update(CarModel carModel) {

        idValidator.validate(carModel.getId());

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
