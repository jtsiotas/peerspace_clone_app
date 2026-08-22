package com.peerspaceClone.backend.dto;

import java.util.Map;

public record ApiErrorResponse(
    String code,
    String message,
    Map<String, String> validationErrors
) {
    public ApiErrorResponse(String code, String message) {
        this(code, message, null);
    }
}
