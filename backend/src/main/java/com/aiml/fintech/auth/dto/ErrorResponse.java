package com.aiml.fintech.auth.dto;

import java.util.List;

public record ErrorResponse(String message, List<FieldError> errors) {

    public record FieldError(String field, String message) {}

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message, null);
    }

    public static ErrorResponse of(String message, List<FieldError> errors) {
        return new ErrorResponse(message, errors);
    }
}
