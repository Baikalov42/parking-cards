package com.epam.parkingcards.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class BrandRequest {

    public static final String BRAND_NAME_PATTERN = "[A-Za-z]{2,29}";

    private long id;

    @NotEmpty(message = "Name field must not be empty")
    @Pattern(regexp = BRAND_NAME_PATTERN)
    private String name;
}
