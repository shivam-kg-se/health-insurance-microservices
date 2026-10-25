package com.ish.aaa.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlanControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIAE(IllegalArgumentException iae){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
