package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private UserDao userDao;

    public long create(User user) {
        return 0;
    }

    public User findById(long id) {
        return null;
    }

    public User findByEmail(String email) {
        return null;
    }

    public User findByLicensePlate(String licensePlate) {
        return null;
    }

    public Collection<User> findAll(int pageNumber) {
        return null;
    }

    public User update(User car) {
        return null;
    }

    public void assignRole(long userId, long roleId) {

    }

    public void revokeRole(long userId, long roleId) {

    }

    public void deleteById(long id) {

    }
}
