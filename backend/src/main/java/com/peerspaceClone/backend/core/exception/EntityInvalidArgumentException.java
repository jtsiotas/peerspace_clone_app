package com.peerspaceClone.backend.core.exception;

public class EntityInvalidArgumentException extends AppGenericException {

    private static final String DEFAULT_CODE = "INVALID_ARGUMENT";
    public EntityInvalidArgumentException(String code, String message) {
        super(code + "_" + DEFAULT_CODE, message);
    }
}
    