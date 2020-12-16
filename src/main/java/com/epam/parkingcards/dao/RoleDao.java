package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
}
