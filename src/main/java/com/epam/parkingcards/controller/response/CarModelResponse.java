package com.epam.parkingcards.controller.response;

import lombok.Data;

@Data
public class CarModelResponse {

    private long id;
    private String modelName;

    private long carId;
    private String brandName;
}
