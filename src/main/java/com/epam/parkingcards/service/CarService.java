package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.CarDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.service.utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private ModelService modelService;


    @PreAuthorize("hasAuthority('ROLE_admin') or @userSecurity.sameUserId(authentication, #carEntity.getUserEntity.id)")
    public long create(CarEntity carEntity) {

        modelService.validateForExistenceAndNotDeleted(carEntity
                .getModelEntity()
                .getId());

        userService.validateForExistence(carEntity
                .getUserEntity()
                .getId());

        try {
            return carDao.saveAndFlush(carEntity).getId();
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Creation error, Car: %s ", carEntity), e);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_admin') or @userSecurity.hasCar(authentication, #id)")
    public CarEntity findById(long id) {
        IdValidator.validate(id);

        return carDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("By id %d, Car not found", id)));
    }

    public Page<CarEntity> findAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        Page<CarEntity> result = carDao.findAll(pageable);

        if (result.isEmpty()) {
            throw new NotFoundException(
                    String.format("Result is empty, page number = %d, page size = %d", pageNumber, PAGE_SIZE));
        }
        return result;
    }

    @PreAuthorize("hasAuthority('ROLE_admin') or @userSecurity.sameUserId(authentication, #userId)")
    public List<CarEntity> findByUserId(long userId) {
        IdValidator.validate(userId);
        userService.validateForExistence(userId);
        return carDao.findByUserId(userId);
    }

    @PreAuthorize("hasAuthority('ROLE_admin') or @userSecurity.sameUserIdAndHasCar(authentication, #carEntity) ")
    public CarEntity update(CarEntity carEntity) {
        IdValidator.validate(carEntity.getId());

        this.validateForExistence(carEntity.getId());
        userService.validateForExistence(carEntity
                .getUserEntity()
                .getId());

        modelService.validateForExistenceAndNotDeleted(carEntity
                .getModelEntity()
                .getId());

        try {
            return carDao.updateCarWithoutUserId(carEntity);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Updating error, Car: %s", carEntity), e);
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_admin') or @userSecurity.hasCar(authentication, #id)")
    public void deleteById(long id) {

        IdValidator.validate(id);
        validateForExistence(id);

        try {
            findById(id)
                    .getUserEntity()
                    .getCarEntities()
                    .removeIf(x -> x.getId() == id);

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
