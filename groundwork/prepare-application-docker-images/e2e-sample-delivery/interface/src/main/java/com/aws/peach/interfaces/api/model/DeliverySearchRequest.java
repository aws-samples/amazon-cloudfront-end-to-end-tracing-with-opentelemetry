package com.aws.peach.interfaces.api.model;

import com.aws.peach.interfaces.support.DeliveryState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class DeliverySearchRequest {
    @PositiveOrZero
    private Integer pageNo = 0;
    @Positive
    private Integer pageSize = 10;
    @DeliveryState
    private String state;
}
