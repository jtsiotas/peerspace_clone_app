package com.peerspaceClone.backend.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.peerspaceClone.backend.dto.ApiErrorResponse;
import com.peerspaceClone.backend.core.exception.EntityAlreadyExistsException;
import com.peerspaceClone.backend.core.exception.EntityInvalidArgumentException;
import com.peerspaceClone.backend.core.exception.EntityNotFoundException;
import com.peerspaceClone.backend.core.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(ValidationException ex) {
        log.error("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        if (ex.getBindingResult() != null) {
            for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }
        ApiErrorResponse response = new ApiErrorResponse(ex.getCode(), ex.getMessage(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("Entity not found: {}", ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        log.error("Entity already exists: {}", ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EntityInvalidArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityInvalidArgumentException(EntityInvalidArgumentException ex) {
        log.error("Invalid argument error: {}", ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        log.error("HTTP Message not readable: {}", ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse("MALFORMED_JSON", "The request body contains invalid/malformed JSON, or is missing required primitive values (like int fields)");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
