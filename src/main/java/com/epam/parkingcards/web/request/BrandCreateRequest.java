package com.epam.parkingcards.web.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BrandCreateRequest {

    public static final String BRAND_NAME_PATTERN = "[A-Za-z0-9- ]{2,30}";

    @NotEmpty(message = "Brand name, must not be empty.")
    @Pattern(regexp = BRAND_NAME_PATTERN,
            message = "Brand name, must contains only latin letters, numbers, spaces and '-'. ")
    @Size(min = 2, max = 30, message = "Brand name, must be 2 to 30 characters.")
    private String name;
}
