package com.aws.peach.domain.delivery.exception;

import com.aws.peach.domain.delivery.DeliveryId;

public class DeliveryAlreadyExistsException extends DeliveryException {
    private final String msg;

    public DeliveryAlreadyExistsException(DeliveryId deliveryId) {
        super(deliveryId);
        this.msg = String.format("duplicate delivery '%s' already exists", deliveryId.getValue());
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
