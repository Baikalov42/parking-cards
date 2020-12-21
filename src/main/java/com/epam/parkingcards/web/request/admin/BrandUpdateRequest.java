package com.epam.parkingcards.web.request.admin;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class BrandUpdateRequest {

    public static final String BRAND_NAME_PATTERN = "[A-Za-z]{2,29}";

    @Min(value = 1, message = "Id should be greater than zero")
    private long id;

    @NotEmpty(message = "Name field must not be empty")
    @Pattern(regexp = BRAND_NAME_PATTERN)
    private String name;
}