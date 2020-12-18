package com.epam.parkingcards.controller.request;

import com.epam.parkingcards.controller.validation.annotation.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class UserRegistrationRequest {

    public static final String NAME_PATTERN = "[A-Za-z]{2,30}";
    public static final String PHONE_PATTERN = "[+][0-9]{11}";

    @NotEmpty(message = "Name field must not be empty")
    @Pattern(regexp = NAME_PATTERN, message = "Only latin letters")
    @Size(min = 2, max = 30, message = "Use 2 to 30 characters")
    private String firstName;

    @NotEmpty(message = "Last name field must not be empty")
    @Pattern(regexp = NAME_PATTERN, message = "Only latin letters")
    @Size(min = 2, max = 30, message = "Use 2 to 30 characters")
    private String lastName;

    @NotEmpty(message = "Phone number field must not be empty")
    @Size(min = 12, max = 12, message = "Must be 12 symbols.")
    @Pattern(regexp = PHONE_PATTERN, message = "Use only numbers, example : '+79880056400")
    private String phone;

    @Email(message = "Must be in email format")
    @NotEmpty(message = "Model id must not be empty")
    private String email;

    @NotNull
    @NotEmpty(message = "Password must not be empty")
    @Size(min = 4, message = "At least 4 symbols")
    private String password;

    private String confirmPassword;
}
