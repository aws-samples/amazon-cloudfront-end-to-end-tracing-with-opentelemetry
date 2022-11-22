package com.aws.peach.interfaces.api.model;

import com.aws.peach.application.CreateDeliveryInput;
import com.aws.peach.domain.delivery.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@ToString
@Getter
public class ReceiveDeliveryOrderRequest {
    @NotNull
    private final String orderNo;
    @NotEmpty
    private final String ordererId;

    private final String ordererName;
    @NotEmpty
    private final List<OrderLine> orderLines;
    @NotNull
    private final String orderDate;
    @NotNull
    private final ShippingInfo shippingInformation;

    public static CreateDeliveryInput newCreateDeliveryInput(ReceiveDeliveryOrderRequest req) {
        List<CreateDeliveryInput.OrderProductDto> orderProducts = req.orderLines.stream()
                .map(OrderLine::newOrderProductDto)
                .collect(Collectors.toList());

        CreateDeliveryInput.OrderDto order = CreateDeliveryInput.OrderDto.builder()
                .id(req.orderNo)
                .createdAt(Instant.parse(req.orderDate))
                .ordererId(req.ordererId)
                .ordererName(req.ordererName)
                .products(orderProducts)
                .build();

        Address receiver = Address.builder()
                    .name(req.shippingInformation.recipient)
                    .telephone(req.shippingInformation.telephoneNumber)
                    .city(req.shippingInformation.city)
                    .address1(req.shippingInformation.address1)
                    .address2(req.shippingInformation.address2)
                    .build();

        return CreateDeliveryInput.builder()
                .order(order)
                .receiver(receiver)
                .build();
    }
    @Builder
    @Getter
    public static class OrderLine {
        @NotNull
        private final String productName;
        private final int quantity;

        public static CreateDeliveryInput.OrderProductDto newOrderProductDto(OrderLine o) {
            return CreateDeliveryInput.OrderProductDto.builder()
                    .name(o.productName)
                    .quantity(o.quantity)
                    .build();
        }
    }
    @Builder
    @Getter
    public static class ShippingInfo {
        @NotNull
        private final String city;
        @NotNull
        private final String telephoneNumber;
        @NotNull
        private final String recipient;
        @NotNull
        private final String address1;
        private final String address2;
    }
}
