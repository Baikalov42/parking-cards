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

    public long create(BrandEntity brandEntity) {

        validateForNameAlreadyUsedAndDeleted(brandEntity.getName());
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
        List<BrandEntity> result = brandDao.findByIsDeletedFalse(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    public List<BrandEntity> findAllDeleted(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<BrandEntity> result = brandDao.findAllDeleted(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    public BrandEntity update(BrandEntity brandEntity) {

        idValidator.validate(brandEntity.getId());
        validateForExistenceAndNotDeleted(brandEntity.getId());
        validateForNameAlreadyUsedAndDeleted(brandEntity.getName());

        try {
            return brandDao.saveAndFlush(brandEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, brand: %s", brandEntity), e);
        }
    }

    public void deleteSoftById(long id) {
        idValidator.validate(id);
        validateForExistenceAndNotDeleted(id);

        try {
            brandDao.markAsDeleted(id);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: id=%d ", id), e);
        }
    }

    public void restore(long id) {
        idValidator.validate(id);
        validateForExistence(id);
        try {
            brandDao.restore(id);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Restoring error: id:%d", id), e);
        }
    }

    private void validateForNameAlreadyUsedAndDeleted(String brandName) {
        long l = brandDao.getCountDeletedByName(brandName);
        if (l > 0) {
            throw new ValidationException(String.format("Name %s already used, and was deleted", brandName));
        }
    }

    public void validateForExistence(long id) {
        if (!brandDao.existsById(id)) {
            throw new ValidationException(String.format("Not exist, id=%d", id));
        }
    }

    public void validateForExistenceAndNotDeleted(long id) {
        BrandEntity brandEntity = findById(id);
        if (brandEntity.isDeleted()) {
            throw new ValidationException(String.format("Brand id=%d marked as deleted", id));
        }
    }
}
