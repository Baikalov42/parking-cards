package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void given_userDao_when_findUserByPlate_then_returnValidUser() {
        Optional<User> found = userDao.findByLicensePlate("O666OO178");
        assertThat(found.isPresent());
        assertThat(found.get().getFirstName()).isEqualTo("Irina");
    }
    @Test
    public void when_updateUserWithNoPasswordProvided_shouldNotUpdatePassword() {
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setFirstName("Roman");
        updateUser.setLastName("Romanov");
        updateUser.setEmail("roma@mail.ru");
        updateUser.setPhone("+79219213454");

        userDao.updateWithoutPasswordAndCars(updateUser);
        Optional<User> updated = userDao.findById(1L);
        assertThat(updated.isPresent());
        assertThat(updated.get().getFirstName()).isEqualTo("Roman");
        assertThat(updated.get().getCars()).isNotEmpty();
        assertThat(updated.get().getCars()).isNotNull();
        assertThat(updated.get().getPassword()).isNotNull();
        assertThat(updated.get().getPassword()).isNotEmpty();
    }

    @Test
    public void testFindAll() {
        assertThat(userDao.findAll().size() == 3);
    }

    @Test
    public void testGetOne() {
        assertThat(userDao.getOne(4L).getRoles().size()).isEqualTo(2);
    }
}
