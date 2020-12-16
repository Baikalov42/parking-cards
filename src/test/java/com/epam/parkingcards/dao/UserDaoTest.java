package com.epam.parkingcards.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testFindAll() {
        assertThat(userDao.findAll().size() == 3);
    }

    @Test
    public void testGetOne() {
        assertThat(userDao.getOne(4L).getRoles().size()).isEqualTo(2);
    }
}