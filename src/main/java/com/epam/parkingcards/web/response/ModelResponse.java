package com.epam.parkingcards.web.response;

import lombok.Data;

@Data
public class ModelResponse {

    private long id;
    private String name;
    private BrandResponse brand;
}
