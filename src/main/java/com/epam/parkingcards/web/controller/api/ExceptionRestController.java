package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.web.response.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(annotations = RestController.class)
public class ExceptionRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionRestController.class);

    @ExceptionHandler({DaoException.class})
    public ResponseEntity<Object> handleDaoException(DaoException ex) {
        LOGGER.error("Dao error", ex);

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "Data base error", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        LOGGER.error("Validation error", ex);

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST, "Validation failed", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        LOGGER.error("Not found error", ex);

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.NO_CONTENT, "Content not found", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<Object> handleBindException(BindException ex) {
        LOGGER.error("Bind validation error", ex);
        StringBuilder message = new StringBuilder();

        for (ObjectError error : ex.getAllErrors()) {
            message.append(error.getDefaultMessage())
                    .append(" ");
        }

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST, "Validation error", message.toString());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Object> handleOthers(Throwable ex) {
        LOGGER.error("Unknown error", ex);

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong!", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
