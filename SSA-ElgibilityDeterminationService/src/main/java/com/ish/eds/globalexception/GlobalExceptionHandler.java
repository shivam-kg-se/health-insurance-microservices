package com.ish.eds.globalexception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException exception, HttpServletRequest request) {
        return errorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception exception, HttpServletRequest request) {
        String message;
        if (exception instanceof MethodArgumentTypeMismatchException mismatchException) {
            message = mismatchException.getName() + " must have a valid value.";
        } else if (exception instanceof HttpMessageNotReadableException) {
            message = "The request contains invalid or incomplete data.";
        } else if (exception instanceof ConstraintViolationException violationException) {
            message = violationException.getConstraintViolations().stream()
                    .findFirst()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .orElse("One or more request values are invalid.");
        } else {
            message = exception.getMessage();
        }
        return errorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationFailure(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(this::validationMessage)
                .orElse("One or more request values are invalid.");
        return errorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(
            Exception exception, HttpServletRequest request) {
        logger.error("Unexpected error while processing request {}", request.getRequestURI(), exception);
        return errorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong while processing your request. Please try again later.",
                request);
    }

    private String validationMessage(FieldError fieldError) {
        return fieldError.getField() + ": "
                + (fieldError.getDefaultMessage() == null
                ? "the value is invalid."
                : fieldError.getDefaultMessage());
    }

    private ResponseEntity<ApiError> errorResponse(
            HttpStatus status, String message, HttpServletRequest request) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }

    public record ApiError(Instant timestamp, int status, String error, String message, String path) {
    }
}
