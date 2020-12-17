package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.CarModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CarModelDaoTest {

    @Autowired
    private CarModelDao carModelDao;

    @Test
    public void given_carModelDao_when_loadingDeleted_then_sizeIsCorrect() {
        CarModel deletedModel = carModelDao.getOne(1L);
        deletedModel.setDeleted(true);
        carModelDao.saveAndFlush(deletedModel);
        assertThat(carModelDao.findByIsDeletedTrue().size()).isEqualTo(1);
        assertThat(carModelDao.findByIsDeletedFalse().size()).isEqualTo(10);
    }

}
