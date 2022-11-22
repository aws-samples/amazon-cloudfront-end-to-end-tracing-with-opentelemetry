package com.aws.peach.domain.delivery;

import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "value")
public class DeliveryId implements Serializable {
    private Long value;
}
