package com.aws.peach.domain.delivery;

import lombok.*;

@Getter // for producer(jsonSerialize). can be replace with @JsonProperty("field_name") on each field
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) // for consumer(jsonDeserialize)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for producer
public class Address {
    private String name;
    private String city;
    private String telephone;
    private String address1;
    private String address2;
}
