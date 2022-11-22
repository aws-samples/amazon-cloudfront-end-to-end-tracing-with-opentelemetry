package com.aws.peach.domain.delivery.exception;

import com.aws.peach.domain.delivery.DeliveryId;

public abstract class DeliveryException extends RuntimeException {
    protected final DeliveryId deliveryId;

    public DeliveryException(DeliveryId deliveryId) {
        this.deliveryId = deliveryId;
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    @Override
    public abstract String getMessage();
}
