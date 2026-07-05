package com.peerspaceClone.backend.core.exception;
import org.springframework.validation.BindingResult;
import lombok.Getter;
@Getter
public class ValidationException extends AppGenericException {

    private static final String DEFAULT_CODE = "VALIDATION_ERROR";
    private final BindingResult bindingResult;

    public ValidationException(String code, String message, BindingResult bindingResult) {
        super(code + DEFAULT_CODE, message);
        this.bindingResult = bindingResult;
    }
}