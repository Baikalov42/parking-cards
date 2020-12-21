package com.epam.parkingcards.dao;

import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.CarEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CarEntityDaoTest {
    @Autowired
    CarDao carDao;

    @Autowired
    ModelDao modelDao;

    @Test
    public void when_carUpdated_then_nestedObjectsNotNull() {
        CarEntity entityCarEntity = carDao.findById(2L)
                .orElseThrow(() -> new NotFoundException("There is no Car with Id 1"));

        CarEntity newCarEntity = new CarEntity();

        newCarEntity.setId(entityCarEntity.getId());
        newCarEntity.setUserEntity(null);
        newCarEntity.setLicensePlate("A651BH178");
        newCarEntity.setModelEntity(modelDao.getOne(2L));

        CarEntity updatedCarEntity = carDao.updateCarWithoutUserId(entityCarEntity);

        assertThat(updatedCarEntity.getModelEntity()).isNotNull();
        assertThat(updatedCarEntity.getModelEntity().getBrandEntity()).isNotNull();
    }
}
