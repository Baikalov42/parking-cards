package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BrandDao extends JpaRepository<BrandEntity, Long> {
    @Query("SELECT b FROM BrandEntity b WHERE b.isDeleted = true")
    Page<BrandEntity> findAllDeleted(Pageable pageable);

    @Query("SELECT COUNT (b) FROM BrandEntity b WHERE b.name = :brandName AND b.isDeleted = true")
    Long getCountDeletedByName(String brandName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE BrandEntity b SET b.isDeleted = true WHERE b.id = :brandId")
    void markAsDeleted(long brandId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE BrandEntity b SET b.isDeleted = false WHERE b.id = :brandId")
    void restore(long brandId);
}
