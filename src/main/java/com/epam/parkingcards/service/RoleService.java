package com.epam.parkingcards.service;

import com.epam.parkingcards.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private UserDao userDao;
}