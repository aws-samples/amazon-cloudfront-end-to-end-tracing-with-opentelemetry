package com.aws.peach.interfaces.common;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {

    private final BindingResult errors;

    public ValidationException(BindingResult errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return this.getMessages().toString();
    }

    public List<String> getMessages() {
        return getValidationMessage(this.errors);
    }

    private List<String> getValidationMessage(BindingResult errors) {
        return errors.getAllErrors()
                .stream()
                .map(ValidationException::getValidationMessage)
                .collect(Collectors.toList());
    }

    private static String getValidationMessage(ObjectError error) {
        if (error instanceof FieldError) {
            FieldError fieldError = (FieldError) error;
            String className = fieldError.getObjectName();
            String property = fieldError.getField();
            Object invalidValue = fieldError.getRejectedValue();
            String message = fieldError.getDefaultMessage();
            return String.format("%s.%s %s, but it was %s", className, property, message, invalidValue);
        }
        return String.format("%s: %s", error.getObjectName(), error.getDefaultMessage());
    }
}
