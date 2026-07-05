package com.peerspaceClone.backend.core.exception;

public class EntityAlreadyExistsException extends AppGenericException {
    private static final String DEFAULT_CODE = "ALREADY_EXISTS";
    public EntityAlreadyExistsException(String code, String message) {
        super(code + "_" + DEFAULT_CODE, message);
    }
    
}
    