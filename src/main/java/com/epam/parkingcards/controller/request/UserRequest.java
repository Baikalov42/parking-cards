package com.epam.parkingcards.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRequest {

    private static final String NAME_PATTERN = "[A-Za-z]{2,29}";

    @Pattern(regexp = NAME_PATTERN, message = "only latin letters")
    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;

    @Pattern(regexp = NAME_PATTERN)
    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    @Pattern(regexp = NAME_PATTERN)
    @NotNull
    @Size(min = 2, max = 30)
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;

}
