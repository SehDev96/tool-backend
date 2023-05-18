package com.map.toolbackend.exceptions;

import com.map.toolbackend.model.ErrorResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalServerErrorException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponseModel(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An Unexpected Error has Occured!"
        ),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
