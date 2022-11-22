package com.aws.peach.interfaces.api;

import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException;
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException;
import com.aws.peach.domain.delivery.exception.DeliveryStateException;
import com.aws.peach.interfaces.api.model.ErrorResponse;
import com.aws.peach.interfaces.common.ErrorCode;
import com.aws.peach.interfaces.common.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.aws.peach.interfaces.api")
public class DeliveryControllerAdvice {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handle(MissingServletRequestParameterException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ValidationException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(ErrorCode.INVALID_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DeliveryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(DeliveryAlreadyExistsException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(ErrorCode.DELIVERY_DUPLICATE, e.getMessage());
    }

    @ExceptionHandler(DeliveryNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(DeliveryNotFoundException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(ErrorCode.DELIVERY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DeliveryStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(DeliveryStateException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(ErrorCode.DELIVERY_ILLEGAL_STATE, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handle(Exception e) {
        log.error("uncaught exception:", e);
        return new ErrorResponse(ErrorCode.UNDEFINED, "Please contact administrator");
    }
}
