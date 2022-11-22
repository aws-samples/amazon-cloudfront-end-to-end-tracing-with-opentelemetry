package com.aws.peach.domain.product.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @Column(name = "product_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;
}
