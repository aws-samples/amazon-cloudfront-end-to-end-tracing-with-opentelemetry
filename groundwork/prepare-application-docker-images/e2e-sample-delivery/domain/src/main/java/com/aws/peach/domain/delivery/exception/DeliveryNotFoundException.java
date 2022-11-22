package com.aws.peach.domain.delivery.exception;

import com.aws.peach.domain.delivery.DeliveryId;

public class DeliveryNotFoundException extends DeliveryException {
    private final String msg;

    public DeliveryNotFoundException(DeliveryId deliveryId) {
        super(deliveryId);
        this.msg = String.format("delivery '%s' is not found", deliveryId.getValue());
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
