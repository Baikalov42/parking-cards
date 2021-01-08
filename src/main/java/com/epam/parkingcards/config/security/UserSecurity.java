package com.epam.parkingcards.config.security;

import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    @Autowired
    private UserService userService;

    public boolean sameUserId(Authentication authentication, Long userId) {
        UserEntity userEntity = userService.findByEmail(authentication.getName());
        return userEntity.getId() == userId;
    }

    public boolean hasCar(Authentication authentication, Long carId) {

        UserEntity userEntity = userService.findByEmail(authentication.getName());
        for (CarEntity carEntity : userEntity.getCarEntities()) {
            if (carEntity.getId() == carId) {
                return true;
            }
        }
        return false;
    }

    public boolean sameUserIdAndHasCar(Authentication authentication, CarEntity carEntity) {
        UserEntity userEntity = userService.findByEmail(authentication.getName());

        boolean isSameUserId = userEntity.getId() == carEntity.getUserEntity().getId();
        boolean userHasCarId = false;

        for (CarEntity currentCar : userEntity.getCarEntities()) {
            if (currentCar.getId() == carEntity.getId()) {
                userHasCarId = true;
                break;
            }
        }
        return isSameUserId && userHasCarId;
    }
}
