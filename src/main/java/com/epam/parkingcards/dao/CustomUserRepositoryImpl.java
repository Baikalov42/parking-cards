package com.epam.parkingcards.dao;

import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CustomUserRepositoryImpl implements CustomUserRepository {
    @Autowired
    UserDao userDao;

    @Override
    public void updateWithoutPasswordAndCars(User user) {
        Optional<User> entityUser = userDao.findById(user.getId());
        user.setPassword(entityUser.orElseThrow(() -> new NotFoundException("No such user!")).getPassword());
        user.setCars(entityUser.orElseThrow(() -> new NotFoundException("No such user!")).getCars());
        userDao.saveAndFlush(user);
    }
}
