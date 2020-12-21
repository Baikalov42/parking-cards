package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.ModelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//TODO implement all methods with @Query annotation
public interface ModelDao extends JpaRepository<ModelEntity, Long> {

    Page<ModelEntity> findAllDeleted(Pageable pageable);

    Page<ModelEntity> findByBrandId(long brandId, Pageable pageable);

    //TODO вернуть количесво строк, где имя = modelName, и deleted = true
    Long getCountDeletedByName(String modelName);

    void markAsDeleted(long modelId);
}