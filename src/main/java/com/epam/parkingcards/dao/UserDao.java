package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends JpaRepository<User, Long> {

}
