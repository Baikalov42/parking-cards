package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.ModelEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ModelEntityDaoTest {

    @Autowired
    private ModelDao modelDao;

    @Test
    public void given_carModelDao_when_loadingDeleted_then_sizeIsCorrect() {
        assertThat(modelDao.findByIsDeletedFalse().size()).isEqualTo(11);
        ModelEntity deletedModel = modelDao.getOne(1L);
        deletedModel.setDeleted(true);
        modelDao.saveAndFlush(deletedModel);
        assertThat(modelDao.findByIsDeletedFalse().size()).isEqualTo(10);
    }

}
