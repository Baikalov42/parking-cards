package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RoleDao extends JpaRepository<UserRole, Long> {
}
