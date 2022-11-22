package com.aws.peach.infrastructure.rest.api;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DeliveryResponse {
    private String deliveryId;
    private String status;
    private String updatedAt;
}
