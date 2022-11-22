package com.aws.peach.application.order.dto;

import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.vo.OrderState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.NONE)
public class OrderDTO {
    private String orderNumber;
    private String deliveryId;
    private List<OrderLineDTO> orderLines;
    private String ordererId;
    private String ordererName;
    private OrderState orderState;
    private Instant createdDateTime;
    private ShippingDTO shippingInfo;

    public OrderLinesSummary getOrderLinesSummary() {
        return new OrderLinesSummary(this.orderLines);
    }

    public static OrderDTO newInstance(Orders entity) {
        List<OrderLineDTO> orderLinesDto = entity.getOrderLines().stream()
                .map(OrderLineDTO::newInstance)
                .collect(Collectors.toList());

        OrderDTO dto = new OrderDTO();
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setDeliveryId(entity.getDeliveryId());
        dto.setOrderLines(orderLinesDto);
        dto.setOrdererId(entity.getCustomerId());
        dto.setOrdererName(entity.getCustomerName());
        dto.setOrderState(entity.getOrderState());
        dto.setCreatedDateTime(entity.getOrderDateTime());
        dto.setShippingInfo(ShippingDTO.newInstance(entity.getShippingInformation()));
        return dto;
    }

    public static final class OrderLinesSummary {
        @Getter private final String orderedProductNameAndQuantities;
        @Getter private final int totalPrice;

        private OrderLinesSummary(final List<OrderLineDTO> orderLines) {
            final StringBuilder orderedProductNameAndQuantities = new StringBuilder();
            final AtomicInteger totalPrice = new AtomicInteger(0);
            final AtomicInteger totalQuantity = new AtomicInteger(0);
            orderLines.forEach(orderLine -> {
                orderedProductNameAndQuantities.append(orderLine.getProductNameAndQuantity());
                totalPrice.addAndGet(orderLine.calculateAmounts());
                totalQuantity.addAndGet(orderLine.getQuantity());
            });
            this.orderedProductNameAndQuantities = orderedProductNameAndQuantities.toString();
            this.totalPrice = totalPrice.get();
        }
    }
}
