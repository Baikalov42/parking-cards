package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandDao extends JpaRepository<BrandEntity, Long> {
    List<BrandEntity> findByIsDeletedFalse();
}
