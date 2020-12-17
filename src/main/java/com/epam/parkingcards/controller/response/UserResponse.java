package com.epam.parkingcards.controller.response;

import lombok.Data;

@Data
public class UserResponse {

    private long id;
    private int carsCount;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

}
