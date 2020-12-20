package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
