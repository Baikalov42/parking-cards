package com.epam.parkingcards.web.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiErrorResponse {

    private HttpStatus status;
    private String shortMessage;
    private String detailedMessage;

    public ApiErrorResponse(HttpStatus status, String shortMessage, String detailedMessage) {
        this.status = status;
        this.shortMessage = shortMessage;
        this.detailedMessage = detailedMessage;
    }
}
