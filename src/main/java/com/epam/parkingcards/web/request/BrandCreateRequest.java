package com.epam.parkingcards.web.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class BrandCreateRequest {

    public static final String BRAND_NAME_PATTERN = "[A-Za-z-]{2,29}";

    @NotEmpty(message = "Brand name, must not be empty.")
    @Pattern(regexp = BRAND_NAME_PATTERN,
            message = "Brand name, must contains only latin letters, and '-'. ")
    private String name;
}
