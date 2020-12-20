package com.epam.parkingcards.web.response;

import lombok.Data;

@Data
public class CarResponse {

    private long id;
    private String licensePlate;
    private CarModelResponse model;
}
