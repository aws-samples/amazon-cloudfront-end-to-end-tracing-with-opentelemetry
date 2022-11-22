package com.aws.peach.infrastructure.rest.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeliveryCreateRequest {
    private String orderNo;
    private String ordererId;
    private String ordererName;
    private List<OrderLine> orderLines;
    private String orderDate;
    private ShippingInfo shippingInformation;

    @Getter
    @Setter
    public static class OrderLine {
        private String productName;
        private int quantity;
    }

    @Getter
    @Setter
    public static class ShippingInfo {
        private String city;
        private String telephoneNumber;
        private String recipient;
        private String address1;
        private String address2;
    }
}
