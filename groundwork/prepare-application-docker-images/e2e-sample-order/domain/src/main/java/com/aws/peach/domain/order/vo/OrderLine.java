package com.aws.peach.domain.order.vo;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @Column(name = "order_line_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "order_seq")
    private Long id;

    @Embedded
    private OrderNumber orderNumber;

    @Embedded
    private OrderProduct orderProduct;

    @Column(name = "quantity")
    private int quantity;
}
