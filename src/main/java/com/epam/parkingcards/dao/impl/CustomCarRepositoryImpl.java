package com.epam.parkingcards.dao.impl;

import com.epam.parkingcards.dao.CustomCarRepository;
import com.epam.parkingcards.model.CarEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomCarRepositoryImpl implements CustomCarRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public CarEntity updateCarWithoutUserId(CarEntity carEntity) {

        CarEntity entityCarEntity = entityManager.find(CarEntity.class, carEntity.getId());
        entityCarEntity.setModelEntity(carEntity.getModelEntity());
        entityCarEntity.setLicensePlate(carEntity.getLicensePlate());
        entityCarEntity.setUserEntity(carEntity.getUserEntity());

        entityManager.merge(entityCarEntity);
        entityManager.flush();
        return entityCarEntity;
    }
}
