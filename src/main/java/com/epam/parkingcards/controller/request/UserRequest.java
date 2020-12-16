package com.epam.parkingcards.controller.request;

import lombok.Data;

@Data
public class UserRequest {

    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;

}
