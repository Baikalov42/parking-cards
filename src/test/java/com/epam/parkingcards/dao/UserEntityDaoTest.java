package com.epam.parkingcards.dao;

import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserEntityDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void when_findUserByPlate_should_returnValidUser() {
        Optional<UserEntity> found = userDao.findByLicensePlate("O666OO178");
        assertThat(found.isPresent());
        assertThat(found.get().getFirstName()).isEqualTo("Irina");
    }
    @Test
    public void when_updateUserWithNoPasswordProvided_shouldNotUpdatePassword() {
        UserEntity updateUserEntity = new UserEntity();
        updateUserEntity.setId(1L);
        updateUserEntity.setFirstName("Roman");
        updateUserEntity.setLastName("Romanov");
        updateUserEntity.setEmail("roma@mail.ru");
        updateUserEntity.setPhone("+79219213454");

        userDao.updateWithoutPasswordAndCars(updateUserEntity);
        UserEntity updated = userDao.findById(1L).orElseThrow(() -> new NotFoundException("There is no user with id 1"));
        assertThat(updated.getFirstName()).isEqualTo("Roman");
        assertThat(updated.getCarEntities()).isNotEmpty();
        assertThat(updated.getCarEntities()).isNotNull();
        assertThat(updated.getPassword()).isNotNull();
        assertThat(updated.getPassword()).isNotEmpty();
    }
}
