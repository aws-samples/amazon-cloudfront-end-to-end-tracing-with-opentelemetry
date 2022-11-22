package com.aws.peach.domain.delivery.exception;

import com.aws.peach.domain.delivery.DeliveryId;

public class DeliveryStateException extends DeliveryException {
    private final String msg;

    public DeliveryStateException(DeliveryId deliveryId) {
        super(deliveryId);
        this.msg = String.format("request is invalid for the delivery's ('%s') current state", deliveryId.getValue());
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
