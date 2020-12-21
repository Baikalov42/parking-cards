package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.model.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelDao extends JpaRepository<ModelEntity, Long> {
    public List<ModelEntity> findByBrandEntity(BrandEntity brandEntity);

    List<ModelEntity> findByIsDeletedFalse();

}
