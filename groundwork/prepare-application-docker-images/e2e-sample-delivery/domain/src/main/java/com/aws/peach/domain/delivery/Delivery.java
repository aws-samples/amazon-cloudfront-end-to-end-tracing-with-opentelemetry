package com.aws.peach.domain.delivery;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "delivery")
@Getter
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_generator")
    @SequenceGenerator(name = "delivery_generator", sequenceName = "delivery_seq")
    @Column(name = "delivery_id")
    private Long id;
    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "order_no")
    private Order order;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "city", column = @Column(name = "sender_city")),
            @AttributeOverride(name = "telephone", column = @Column(name = "sender_telephone")),
            @AttributeOverride(name = "address1", column = @Column(name = "sender_address1")),
            @AttributeOverride(name = "address2", column = @Column(name = "sender_address2"))
    })
    private Address sender;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "receiver_name")),
            @AttributeOverride(name = "city", column = @Column(name = "receiver_city")),
            @AttributeOverride(name = "telephone", column = @Column(name = "receiver_telephone")),
            @AttributeOverride(name = "address1", column = @Column(name = "receiver_address1")),
            @AttributeOverride(name = "address2", column = @Column(name = "receiver_address2"))
    })
    private Address receiver;
    @Embedded
    private DeliveryStatus status;
    @OneToMany(mappedBy = "delivery", cascade = ALL)
    private List<DeliveryItem> items;

    public Delivery(Order order, Address sender, Address receiver) {
        this.order = order; // todo validate (not null)
        this.sender = sender; // todo validate (not null)
        this.receiver = receiver; // todo validate (not null)
        this.status = new DeliveryStatus(DeliveryStatus.Type.PREPARING);
        this.items = new ArrayList<>();
    }

    public void ship() {
        this.status = new DeliveryStatus(DeliveryStatus.Type.SHIPPED);
    }

    public Long getId() {
        return id;
    }

    public String getIdString() {
        return String.valueOf(id);
    }

    public String getOrderNo() {
        return this.order.getOrderNo();
    }

    public void addDeliveryItem(DeliveryItem item) {
        this.items.add(item);
        item.setDelivery(this);
    }
}
