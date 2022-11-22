package com.aws.peach.domain.order.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Builder
@Getter
@Embeddable
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInformation {
    @Column(name = "city")
    private String city;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;
}
