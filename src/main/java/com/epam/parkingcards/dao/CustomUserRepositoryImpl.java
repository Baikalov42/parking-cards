package com.epam.parkingcards.dao;

import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomUserRepositoryImpl implements CustomUserRepository {
    @Autowired
    UserDao userDao;

    @Override
    public User updateWithoutPasswordAndCars(User user) {
        User entityUser = userDao.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(String.format("There is no user with id = %s", user.getId())));
        user.setPassword(entityUser.getPassword());
        user.setCars(entityUser.getCars());
        userDao.saveAndFlush(user);
        return user;
    }
}
