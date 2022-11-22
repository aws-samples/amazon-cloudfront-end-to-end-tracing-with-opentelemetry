package com.aws.peach.application;

import com.aws.peach.domain.delivery.*;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class CreateDeliveryInput {
    private OrderDto order;
    private Address receiver;

    public static Delivery newDelivery(CreateDeliveryInput o, Address sender) {
        Order.Orderer orderer = new Order.Orderer(o.order.ordererId, o.order.ordererName);
        Order order = Order.builder()
                .orderNo(o.order.id)
                .openedAt(o.order.createdAt)
                .orderer(orderer)
                .build();
        Delivery delivery = new Delivery(order, sender, o.receiver);
        List<DeliveryItem> items = o.order.products.stream()
                .map(OrderProductDto::newDeliveryItem)
                .collect(Collectors.toList());
        for (DeliveryItem item : items) {
            delivery.addDeliveryItem(item);
        }
        return delivery;
    }

    @Builder
    @Getter
    public static class OrderDto {
        private final String id;
        private final Instant createdAt;
        private final String ordererId;
        private final String ordererName;
        private final List<OrderProductDto> products;
    }

    @Builder
    public static class OrderProductDto {
        private final String name;
        private final int quantity;

        public static DeliveryItem newDeliveryItem(OrderProductDto o) {
            return DeliveryItem.builder()
                    .name(o.name)
                    .quantity(o.quantity)
                    .build();
        }
    }
}
