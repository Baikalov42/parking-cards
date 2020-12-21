package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
