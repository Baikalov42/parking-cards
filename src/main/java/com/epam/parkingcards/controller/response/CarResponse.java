package com.epam.parkingcards.controller.response;

import lombok.Data;

@Data
public class CarResponse {

    private long id;
    private String licensePlate;

    private long userId;
    private String userEmail;

    private long modelId;
    private String modelName;
}
