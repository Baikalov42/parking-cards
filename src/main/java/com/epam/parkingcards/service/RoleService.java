package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private UserDao userDao;

    public long create(Role role) {
        return 0;
    }

    public Role findById(long id) {
        return null;
    }

    public Collection<Role> findAll(int pageNumber) {
        return null;
    }

    public Role update(Role role) {
        return null;
    }

    public void deleteById(long id) {

    }
}
