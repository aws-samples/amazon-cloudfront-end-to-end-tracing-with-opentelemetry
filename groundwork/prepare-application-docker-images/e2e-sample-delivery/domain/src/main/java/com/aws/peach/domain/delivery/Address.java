package com.aws.peach.domain.delivery;

import lombok.*;

import javax.persistence.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Column(name = "name")
    private String name;
    @Column(name = "city")
    private String city;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "address1")
    private String address1;
    @Column(name = "address2")
    private String address2;
}
