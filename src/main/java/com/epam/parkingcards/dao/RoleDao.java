package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface RoleDao extends CrudRepository<UserRole, Long> {
}
