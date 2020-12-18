package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.User;

public interface CustomUserRepository {
    User updateWithoutPasswordAndCars(User user);
}
