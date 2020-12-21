package com.epam.parkingcards.dao;

import com.epam.parkingcards.model.UserEntity;

public interface CustomUserRepository {
    UserEntity updateWithoutPasswordAndCars(UserEntity userEntity);
}
