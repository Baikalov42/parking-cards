package com.epam.parkingcards.web.response;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {

    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Set<CarResponse> cars;

}
