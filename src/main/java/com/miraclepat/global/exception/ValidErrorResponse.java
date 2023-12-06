package com.miraclepat.global.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ValidErrorResponse extends ErrorResponse{

    private final List<ValidationError> errors;

    private ValidErrorResponse(ErrorCode errorCode, String errorMessage, List<ValidationError> errors) {
        super(errorCode, errorMessage);
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode errorCode, String errorMessage, List<ValidationError> errors) {
        return new ValidErrorResponse(errorCode, errorMessage != null ? errorMessage : errorCode.getMessage(), errors);
    }

    @Getter
    public static class ValidationError {
        private String field;
        private String message;

        private ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public static List<ValidErrorResponse.ValidationError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new ValidErrorResponse.ValidationError(
                            error.getField(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
