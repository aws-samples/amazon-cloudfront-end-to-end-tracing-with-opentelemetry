package com.aws.peach.domain.order.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "orderNumber")
public class OrderNumber implements Serializable {

    @Column(name = "order_number")
    private String orderNumber;

    public static List<OrderNumber> ofList(List<String> orderNumbers) {
        return orderNumbers.stream()
                .map(OrderNumber::create)
                .collect(Collectors.toList());
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public static OrderNumber create(final String orderNumber) {
        return new OrderNumber(orderNumber);
    }
}