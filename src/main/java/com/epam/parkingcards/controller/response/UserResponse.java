package com.epam.parkingcards.controller.response;

import com.epam.parkingcards.model.Car;
import com.epam.parkingcards.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {

    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Set<Car> cars;
    private Set<Role> roles;

}
