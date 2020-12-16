package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandDao extends JpaRepository<Brand, Long> {
}
