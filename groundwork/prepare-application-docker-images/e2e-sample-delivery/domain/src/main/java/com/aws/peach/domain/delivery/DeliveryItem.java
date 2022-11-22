package com.aws.peach.domain.delivery;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "delivery_item")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_item_generator")
    @SequenceGenerator(name = "delivery_item_generator", sequenceName = "delivery_item_seq")
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Column(name = "item_name")
    private String name;

    @Column(name = "quantity")
    private int quantity;

    void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
