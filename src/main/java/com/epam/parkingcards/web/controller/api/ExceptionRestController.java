package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.web.response.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(annotations = RestController.class)
public class ExceptionRestController {

    @ExceptionHandler({DaoException.class})
    public ResponseEntity<Object> handleDaoException(Exception ex) {

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "Data base error", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleValidationException(Exception ex) {

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST, "Validation failed", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(Exception ex) {

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.NO_CONTENT, "Content not found", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<Object> handleBindException(BindException ex) {
        StringBuilder message = new StringBuilder();

        for (FieldError fieldError : ex.getFieldErrors()) {
            message.append("[")
                    .append(fieldError.getField())
                    .append(":")
                    .append(fieldError.getDefaultMessage())
                    .append("]")
                    .append("\n");
        }
        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST, "Validation error", message.toString());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Object> handleOthers(Throwable ex) {

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong!", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
