package com.aws.peach.domain.order.exception;

public abstract class OrderException extends RuntimeException {
    @Override
    public abstract String getMessage();
}
