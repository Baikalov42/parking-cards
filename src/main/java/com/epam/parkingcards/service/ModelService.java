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

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ModelDao modelDao;
    @Autowired
    private BrandService brandService;
    @Autowired
    private IdValidator idValidator;

    //TODO: check if there is soft deleted model with the same name
    //TODO: if found, set deleted to false
    public long create(ModelEntity modelEntity) {
        try {
            return modelDao.saveAndFlush(modelEntity).getId();
        } catch (DataAccessException e) {
            throw new DaoException("Creation error, model: " + modelEntity, e);
        }
    }

    public ModelEntity findById(long id) {
        idValidator.validate(id);

        return modelDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, Model not found", id)));
    }

    public List<ModelEntity> findAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<ModelEntity> result = modelDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    //TODO: check changed logic - whe retrieve models from CarModelDao, this way we will get only not deleted items
    public List<ModelEntity> findAllByBrand(long brandId) {
        return new ArrayList<>(modelDao.findByBrandEntity(brandService.findById(brandId)));
    }

    //TODO: Think about situation when setting the same name as soft-deleted one
    public ModelEntity update(ModelEntity modelEntity) {

        idValidator.validate(modelEntity.getId());

        validateForExistence(modelEntity.getId());
        brandService.validateForExistence(modelEntity
                .getBrandEntity()
                .getId());

        try {
            return modelDao.saveAndFlush(modelEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, Model: %s", modelEntity), e);
        }
    }

    public void deleteSoftById(long id) {
        idValidator.validate(id);
        try {
            ModelEntity modelEntity = modelDao.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("By id %d, Model not found", id)));

            modelEntity.setDeleted(true);
            modelDao.saveAndFlush(modelEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Deleting error: id=%d ", id), e);
        }
    }

    public void validateForExistence(long id) {
        if (!modelDao.existsById(id)) {
            throw new ValidationException(String.format("Not exist, id=%d", id));
        }
    }
}
