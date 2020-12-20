package com.epam.parkingcards.web.request.me;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class MeCarUpdateRequest {

    private static final String LICENSE_PLATE_PATTERN = "^[ABEKMHOPCTYX][0-9]{3}[ABEKMHOPCTYX]{2}[0-9]{2,3}$";

    @Min(value = 1, message = "Car id should be greater than zero")
    private long id;

    @Min(value = 1, message = "Model id should be greater than zero")
    private long modelId;

    @Pattern(regexp = LICENSE_PLATE_PATTERN, message = "Only latin letters and numbers")
    @NotEmpty(message = "License plate should be not empty")
    @Size(min = 8, max = 9, message = "License plate should be 8 or 9 symbols")
    private String licensePlate;
}
