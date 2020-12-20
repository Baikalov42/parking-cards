package com.epam.parkingcards.dao;

import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomUserRepositoryImpl implements CustomUserRepository {
//    @Autowired
//    UserDao userDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User updateWithoutPasswordAndCars(User user) {
        User entityUser = entityManager.find(User.class, user.getId());
        entityUser.setPhone(user.getPhone());
        entityUser.setFirstName(user.getFirstName());
        entityUser.setLastName(user.getLastName());
        entityUser.setEmail(user.getEmail());
        entityManager.merge(entityUser);
        entityManager.flush();
        return entityUser;
    }
}
