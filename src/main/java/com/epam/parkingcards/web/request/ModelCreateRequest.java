package com.epam.parkingcards.web.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ModelCreateRequest {

    public static final String MODEL_NAME_PATTERN = "[A-Za-z0-9]{2,30}";

    @Min(value = 1, message = "Brand id should be greater than zero")
    private long brandId;

    @NotEmpty(message = "Model name field must not be empty")
    @Pattern(regexp = MODEL_NAME_PATTERN, message = "Only latin letters and numbers")
    @Size(min = 2, max = 30, message = "Use 2 to 30 characters")
    private String name;
}
