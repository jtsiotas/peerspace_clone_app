package com.peerspaceClone.backend.core.exception;

import lombok.Getter;

@Getter
public class AppGenericException extends Exception {
    private final String code;

    public AppGenericException(String code) {
        this.code = code;
    }

    public AppGenericException(String message, String code) {
        super(message);
        this.code = code;
    }
    
}
    
    