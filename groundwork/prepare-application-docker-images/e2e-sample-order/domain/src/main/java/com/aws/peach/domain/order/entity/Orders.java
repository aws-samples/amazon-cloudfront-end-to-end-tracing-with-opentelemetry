package com.aws.peach.domain.order.entity;

import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.order.vo.*;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@EqualsAndHashCode(of = "orderNumber")
public class Orders {
    @EmbeddedId
    private OrderNumber orderNumber;

    @Column(name = "delivery_id")
    private String deliveryId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_number")
    private List<OrderLine> orderLines;

    @Column(name = "member_id")
    private String customerId;

    @Column(name = "member_name")
    private String customerName;

    @Column(name = "order_state")
    @Enumerated(value = EnumType.STRING)
    private OrderState orderState;

    @Column(name = "order_date_time")
    private Instant orderDateTime;

    @Embedded
    private ShippingInformation shippingInformation;

    public String getOrderNumber() {
        return this.orderNumber.getOrderNumber();
    }

    public void close() {
        this.orderState = OrderState.CLOSED;
    }

    public void updateDeliveryId(DeliveryId deliveryId) {
        this.deliveryId = deliveryId.getValue();
    }
}
