package com.aws.peach.domain.delivery;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryStatus {
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Type type;
    @Column(name = "last_updated")
    private Instant timestamp;

    public DeliveryStatus(Type type) {
        this.type = type;
        this.timestamp = Instant.now();
    }

    public enum Type {
        PREPARING, SHIPPED;
    }
}


