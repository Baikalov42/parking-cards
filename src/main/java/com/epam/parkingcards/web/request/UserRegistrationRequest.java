package com.epam.parkingcards.web.request;

import com.epam.parkingcards.web.validation.annotation.PasswordMatches;
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

    @NotEmpty(message = "First name, must not be empty.")
    @Pattern(regexp = NAME_PATTERN, message = "First name must contains, only latin letters.")
    @Size(min = 2, max = 30, message = "For first name, use 2 to 30 characters.")
    private String firstName;

    @NotEmpty(message = "Last name, must not be empty.")
    @Pattern(regexp = NAME_PATTERN, message = "Last name must contains, only latin letters.")
    @Size(min = 2, max = 30, message = "Use 2 to 30 characters.")
    private String lastName;

    @NotEmpty(message = "Phone, must not be empty.")
    @Size(min = 12, max = 12, message = "Phone, must be 12 symbols.")
    @Pattern(regexp = PHONE_PATTERN, message = "Phone, must contains, only + and numbers.")
    private String phone;

    @Email(message = "E-mail, must be right format.")
    @NotEmpty(message = "E-mail, must not be empty.")
    private String email;

    @NotNull
    @NotEmpty(message = "Password, must not be empty.")
    @Size(min = 4, message = "Password, min size 4 symbols.")
    private String password;

    private String confirmPassword;
}
