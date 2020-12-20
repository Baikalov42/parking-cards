package com.epam.parkingcards.controller.handler;

import com.epam.parkingcards.controller.response.error.ApiError;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({DaoException.class})
    public ResponseEntity<Object> handleDaoException(Exception ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Data base error", getExceptionMessageChain(ex));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleValidationException(Exception ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed", getExceptionMessageChain(ex));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.NO_CONTENT, "Content not found", getExceptionMessageChain(ex));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private List<String> getExceptionMessageChain(Throwable throwable) {

        List<String> result = new ArrayList<>();
        while (throwable != null) {
            result.add(throwable.getMessage());
            throwable = throwable.getCause();
        }
        return result;
    }
}
