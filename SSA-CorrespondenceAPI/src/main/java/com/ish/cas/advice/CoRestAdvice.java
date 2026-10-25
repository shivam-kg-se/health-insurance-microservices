package com.ish.cas.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;

@RestControllerAdvice
public class CoRestAdvice {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handelFileNotFoundException(FileNotFoundException fnfe) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fnfe.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handelAllException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
