package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.User;

public interface CustomUserRepository {
    void updateWithoutPasswordAndCars(User user);
}
