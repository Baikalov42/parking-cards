package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelDao extends JpaRepository<Model, Long> {
}
