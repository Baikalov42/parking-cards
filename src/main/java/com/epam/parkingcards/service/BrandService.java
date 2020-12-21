package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.BrandDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private IdValidator idValidator;

    //TODO: check if there is soft deleted brand with the same name
    //TODO: if found, set deleted to false
    public long create(BrandEntity brandEntity) {
        try {
            return brandDao.saveAndFlush(brandEntity).getId();
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Creation error, brand: %s ", brandEntity), e);
        }
    }

    public BrandEntity findById(long id) {
        idValidator.validate(id);

        return brandDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, Car not found", id)));
    }

    public List<BrandEntity> findAll(int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<BrandEntity> result = brandDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    //TODO: Think about situation when setting the same name as soft-deleted one
    public BrandEntity update(BrandEntity brandEntity) {

        idValidator.validate(brandEntity.getId());
        validateForExistence(brandEntity.getId());
        try {
            return brandDao.saveAndFlush(brandEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, brand: %s", brandEntity), e);
        }
    }

    public void deleteSoftById(long id) {
        idValidator.validate(id);
        try {
            BrandEntity brandEntity = brandDao.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("By id %d, CarBrand not found", id)));
            brandEntity.setDeleted(true);
            brandDao.saveAndFlush(brandEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: id=%d ", id), e);
        }
    }

    public void validateForExistence(long id) {
        if (!brandDao.existsById(id)) {
            throw new ValidationException(String.format("Not exist, id=%d", id));
        }
    }
}
