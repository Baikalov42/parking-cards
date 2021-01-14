package com.epam.parkingcards.web.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserUpdateRequest {

    public static final String NAME_PATTERN = "[A-Za-z]{2,30}";
    public static final String PHONE_PATTERN = "[+][0-9]{11}";

    @Min(value = 1, message = "Car id, should be greater than zero.")
    private long id;

    @NotEmpty(message = "First name, must not be empty.")
    @Pattern(regexp = NAME_PATTERN, message = "First name must contains, only latin letters.")
    @Size(min = 2, max = 30, message = "For first name, use 2 to 30 characters.")
    private String firstName;

    @NotEmpty(message = "Last name, must not be empty.")
    @Pattern(regexp = NAME_PATTERN, message = "Last name, must contains, only latin letters.")
    @Size(min = 2, max = 30, message = "For last name, use 2 to 30 characters.")
    private String lastName;

    @NotEmpty(message = "Phone, must not be empty.")
    @Size(min = 12, max = 12, message = "Phone, must be 12 symbols.")
    @Pattern(regexp = PHONE_PATTERN, message = "Phone, must contains, only + and numbers.")
    private String phone;

    @Email(message = "E-mail, must be right format.")
    @NotEmpty(message = "E-mail, must not be empty.")
    private String email;

}
