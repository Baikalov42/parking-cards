package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ModelDao extends JpaRepository<ModelEntity, Long> {
    @Query("SELECT m FROM ModelEntity m WHERE m.isDeleted = true")
    Page<ModelEntity> findAllDeleted(Pageable pageable);

    @Query("SELECT m FROM ModelEntity m WHERE m.brandEntity.id = :brandId AND m.isDeleted = false")
    Page<ModelEntity> findByBrandId(long brandId, Pageable pageable);

    @Query("SELECT m FROM ModelEntity m WHERE m.brandEntity.id = :brandId AND m.isDeleted = false")
    List<ModelEntity> findByBrandId(long brandId);

    @Query("SELECT COUNT (m) FROM ModelEntity m WHERE m.name = :modelName AND m.isDeleted = true")
    Long getCountDeletedByName(String modelName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ModelEntity m SET m.isDeleted = true WHERE m.id = :modelId")
    void markAsDeleted(long modelId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ModelEntity m SET m.isDeleted = false WHERE m.id = :modelId")
    void restore(long modelId);

    Page<ModelEntity> findByIsDeletedFalse(Pageable pageable);

    List<ModelEntity> findByIsDeletedFalse();

    @Query("SELECT m FROM ModelEntity m WHERE LOWER(m.name) LIKE %:keyword%")
    List<ModelEntity> findByKeyword(String keyword);
}