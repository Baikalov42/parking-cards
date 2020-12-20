package com.epam.parkingcards.web.response;

import lombok.Data;

@Data
public class CarModelResponse {

    private long id;
    private String name;
    private CarBrandResponse brand;
}
