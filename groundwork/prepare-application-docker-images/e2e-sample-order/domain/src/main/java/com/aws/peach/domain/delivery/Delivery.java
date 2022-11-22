package com.aws.peach.domain.delivery;

import com.aws.peach.domain.order.vo.OrderNumber;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class Delivery {
    private DeliveryId id;
    private OrderNumber orderNumber;
    private String ordererId;
    private String ordererName;
    private List<Item> items;
    private Instant orderCreatedDateTime;
    private Address shipToAddress;

    public void updateId(DeliveryId id) {
        this.id = id;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Item {
        private String productId;
        private String productName;
        private int quantity;
    }
}
