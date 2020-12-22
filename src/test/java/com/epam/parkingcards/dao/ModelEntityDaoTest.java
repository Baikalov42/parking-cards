package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.ModelEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ModelEntityDaoTest {

    @Autowired
    private ModelDao modelDao;

    @Test
    public void given_carModelDao_when_loadingDeleted_then_sizeIsCorrect() {
        modelDao.markAsDeleted(1);
        PageRequest id = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<ModelEntity> deletedByBrandId = modelDao.findAllDeleted(id).toList();
        assertThat(deletedByBrandId.size()).isEqualTo(1);
    }

}
