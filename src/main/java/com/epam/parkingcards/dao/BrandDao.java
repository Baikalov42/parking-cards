package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//TODO implement all methods with @Query annotation
public interface BrandDao extends JpaRepository<BrandEntity, Long> {

    Page<BrandEntity> findAllDeleted(Pageable pageable);

    //TODO вернуть количесво строк, где имя = brandName, и deleted = true
    Long getCountDeletedByName(String brandName);

    void markAsDeleted(long brandId);
}
