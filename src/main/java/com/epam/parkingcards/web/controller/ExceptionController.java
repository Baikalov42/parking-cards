package com.epam.parkingcards.web.controller;

import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.web.response.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
//TODO DISABLE FOR DEBUG
//@ControllerAdvice
public class ExceptionController {

    //@ExceptionHandler({DaoException.class})
    public ResponseEntity<Object> handleDaoException(Exception ex, WebRequest request) {

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "Data base error", getExceptionMessageChain(ex));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

   // @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleValidationException(Exception ex, WebRequest request) {

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST, "Validation failed", getExceptionMessageChain(ex));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

   // @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.NO_CONTENT, "Content not found", getExceptionMessageChain(ex));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

   // @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleOthers(Exception ex, WebRequest request) {

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong!", getExceptionMessageChain(ex));
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
