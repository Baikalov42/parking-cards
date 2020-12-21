package com.epam.parkingcards.dao.impl;

import com.epam.parkingcards.dao.CustomUserRepository;
import com.epam.parkingcards.model.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public UserEntity updateWithoutPasswordAndCars(UserEntity userEntity) {
        UserEntity entityUserEntity = entityManager.find(UserEntity.class, userEntity.getId());

        entityUserEntity.setPhone(userEntity.getPhone());
        entityUserEntity.setFirstName(userEntity.getFirstName());
        entityUserEntity.setLastName(userEntity.getLastName());
        entityUserEntity.setEmail(userEntity.getEmail());

        entityManager.merge(entityUserEntity);
        entityManager.flush();
        return entityUserEntity;
    }
}
