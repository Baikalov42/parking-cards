package com.epam.parkingcards.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
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
        assertThat(userDao.getOne(1L).getRole().getRole()).isEqualTo("admin");
    }
}
