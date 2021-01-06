package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.ModelDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ModelDao modelDao;
    @Autowired
    private BrandService brandService;;

    public long create(ModelEntity modelEntity) {

        validateForNameAlreadyUsedAndDeleted(modelEntity.getName());

        brandService.validateForExistenceAndNotDeleted(modelEntity
                .getBrandEntity()
                .getId());

        try {
            return modelDao.saveAndFlush(modelEntity).getId();
        } catch (DataAccessException e) {
            throw new DaoException("Creation error, model: " + modelEntity, e);
        }
    }

    public ModelEntity findById(long id) {
        IdValidator.validate(id);

        return modelDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, Model not found", id)));
    }

    public List<ModelEntity> findAllActive(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<ModelEntity> result = modelDao.findByIsDeletedFalse(pageable).getContent();
        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }
//TODO проверка brandID is exist
    public List<ModelEntity> findAllByBrand(long brandId, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<ModelEntity> result = modelDao.findByBrandId(brandId, pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("By Model id=%d, result is empty. Page number = %d, page size = %d",
                            brandId, pageNumber, PAGE_SIZE));
        }
        return result;
    }

    public List<ModelEntity> findAllDeleted(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<ModelEntity> result = modelDao.findAllDeleted(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    public List<ModelEntity> findByKeyword(String keyword) {
        List<ModelEntity> result = modelDao.findByKeyword(keyword.toLowerCase());
        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("By keyword %s, Models not found", keyword));
        }
        return result;
    }

    public ModelEntity update(ModelEntity modelEntity) {

        IdValidator.validate(modelEntity.getId());
        this.validateForExistenceAndNotDeleted(modelEntity.getId());

        brandService.validateForExistenceAndNotDeleted(modelEntity
                .getBrandEntity()
                .getId());

        this.validateForNameAlreadyUsedAndDeleted(modelEntity.getName());

        try {
            return modelDao.saveAndFlush(modelEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, Model: %s", modelEntity), e);
        }
    }

    public void deleteSoftById(long id) {
        IdValidator.validate(id);
        validateForExistenceAndNotDeleted(id);

        try {
            modelDao.markAsDeleted(id);

        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: model id=%d ", id), e);
        }
    }

    public void restore(long id) {
        IdValidator.validate(id);
        validateForExistence(id);
        try {
            modelDao.restore(id);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Restoring error: model id:%d", id), e);
        }
    }

    private void validateForNameAlreadyUsedAndDeleted(String modelName) {
        long l = modelDao.getCountDeletedByName(modelName);
        if (l > 0) {
            throw new ValidationException(String.format("Model name %s already used, and was deleted", modelName));
        }
    }

    public void validateForExistence(long id) {
        if (!modelDao.existsById(id)) {
            throw new ValidationException(String.format("Model not exist, id=%d", id));
        }
    }

    public void validateForExistenceAndNotDeleted(long id) {
        ModelEntity modelEntity = findById(id);

        if (modelEntity.isDeleted()) {
            throw new ValidationException(String.format("Model id=%d marked as deleted", id));
        }
    }
}
