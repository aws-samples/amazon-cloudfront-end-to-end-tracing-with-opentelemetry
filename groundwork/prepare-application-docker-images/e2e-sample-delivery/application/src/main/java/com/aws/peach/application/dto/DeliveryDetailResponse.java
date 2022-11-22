package com.aws.peach.application.dto;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.application.support.DtoUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class DeliveryDetailResponse extends DeliveryResponse {
    private final String deliveryId;
    private final Order order;
    private final List<DeliveryProduct> items;
    private final Address sendingAddress;
    private final Address shippingAddress;
    private final String status;
    private final String updatedAt;

    public static DeliveryDetailResponse of(Delivery delivery) {
        Order order = Order.of(delivery.getOrder());
        List<DeliveryProduct> items = delivery.getItems().stream()
                .map(DeliveryProduct::of)
                .collect(Collectors.toList());
        Address sender = Address.of(delivery.getSender());
        Address receiver = Address.of(delivery.getReceiver());
        return DeliveryDetailResponse.builder()
                .deliveryId(delivery.getIdString())
                .order(order)
                .items(items)
                .sendingAddress(sender)
                .shippingAddress(receiver)
                .status(delivery.getStatus().getType().name())
                .updatedAt(DtoUtil.formatTimestamp(delivery.getStatus().getTimestamp()))
                .build();
    }

    @Builder
    @Getter
    static class Address {
        private final String name;
        private final String city;
        private final String telephone;
        private final String address1;
        private final String address2;

        public static Address of(com.aws.peach.domain.delivery.Address o) {
            return Address.builder()
                    .name(o.getName())
                    .city(o.getCity())
                    .telephone(o.getTelephone())
                    .address1(o.getAddress1())
                    .address2(o.getAddress2())
                    .build();
        }
    }
}
