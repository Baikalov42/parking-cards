package com.epam.parkingcards.config;

import com.epam.parkingcards.model.Car;
import com.epam.parkingcards.model.User;
import com.epam.parkingcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    @Autowired
    private UserService userService;

    public boolean hasUserId(Authentication authentication, Long userId) {
        User user = userService.findByEmail(authentication.getName());
        return user.getId() == userId;
    }

    public boolean hasCarId(Authentication authentication, Long carId) {

        User user = userService.findByEmail(authentication.getName());
        for (Car car : user.getCars()) {
            if (car.getId() == carId) {
                return true;
            }
        }
        return false;
    }
}
