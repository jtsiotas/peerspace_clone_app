package com.peerspaceClone.backend.core.exception;

public class EntityNotFoundException extends AppGenericException {

    private static final String DEFAULT_CODE = "NOT_FOUND";
    public EntityNotFoundException(String code, String message) {
        super(code + "_" + DEFAULT_CODE, message);
    }
    
}