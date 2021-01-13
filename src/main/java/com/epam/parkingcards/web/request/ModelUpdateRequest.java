package com.epam.parkingcards.web.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ModelUpdateRequest {

    public static final String MODEL_NAME_PATTERN = "[A-Za-z0-9- ]{1,30}";

    @Min(value = 1, message = "Model id, should be greater than zero.")
    private long id;

    @Min(value = 1, message = "Brand id, should be greater than zero.")
    private long brandId;

    @NotEmpty(message = "Model name, must not be empty.")
    @Pattern(regexp = MODEL_NAME_PATTERN,
            message = "Model name, must contains only latin letters, numbers, spaces and '-'. ")
    @Size(min = 1, max = 30, message = "Model name, must be 1 to 30 characters.")
    private String name;
}
