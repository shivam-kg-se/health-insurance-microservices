package com.ish.dcs.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class DataCollectionOperationControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
