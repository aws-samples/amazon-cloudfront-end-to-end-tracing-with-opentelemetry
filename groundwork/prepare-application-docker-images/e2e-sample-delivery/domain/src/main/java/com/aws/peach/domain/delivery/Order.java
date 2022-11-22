package com.aws.peach.domain.delivery;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "orderNo")
public class Order {
    @Id
    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "order_created")
    private Instant openedAt;
    @Embedded
    private Orderer orderer;

    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Orderer {
        @Column(name = "orderer_id")
        private String id;
        @Column(name = "orderer_name")
        private String name;
    }
}
