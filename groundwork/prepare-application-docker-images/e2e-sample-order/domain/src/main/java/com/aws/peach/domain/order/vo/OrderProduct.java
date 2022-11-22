package com.aws.peach.domain.order.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Builder
@Getter
@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private int price;
}
/*
 주문 상품 Context 는 Product 의 Context 와 다르다.
 주문 상품은 주문하는 시점의 Product 이다. 즉, 상품  관리 에서 상품의 가격은 상품의 가격과 다르다
 */