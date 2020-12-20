package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarBrandDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.CarBrand;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarBrandService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CarBrandDao carBrandDao;
    @Autowired
    private IdValidator idValidator;

    //TODO: check if there is soft deleted brand with the same name
    //TODO: if found, set deleted to false
    public long create(CarBrand carBrand) {
        try {
            return carBrandDao.saveAndFlush(carBrand).getId();
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Creation error, brand: %s ", carBrand), e);
        }
    }

    public CarBrand findById(long id) {
        idValidator.validate(id);

        return carBrandDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, Car not found", id)));
    }

    public List<CarBrand> findAll(int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<CarBrand> result = carBrandDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    //TODO: Think about situation when setting the same name as soft-deleted one
    public CarBrand update(CarBrand carBrand) {

        idValidator.validate(carBrand.getId());
        validateForExistence(carBrand.getId());
        try {
            return carBrandDao.saveAndFlush(carBrand);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, brand: %s", carBrand), e);
        }
    }

    public void deleteSoftById(long id) {
        idValidator.validate(id);
        try {
            CarBrand carBrand = carBrandDao.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("By id %d, CarBrand not found", id)));
            carBrand.setDeleted(true);
            carBrandDao.saveAndFlush(carBrand);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: id=%d ", id), e);
        }
    }

    public void validateForExistence(long id) {
        if (!carBrandDao.existsById(id)) {
            throw new ValidationException(String.format("Not exist, id=%d", id));
        }
    }
}
