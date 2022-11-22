package com.aws.peach.domain.delivery;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository {
    Optional<Delivery> findById(Long deliveryId);
    Delivery save(Delivery delivery);
    Iterable<Delivery> findAll(Pageable pageable);
    List<Delivery> findAllByStatus(DeliveryStatus.Type type, Pageable pageable);

    Optional<Delivery> findByOrderNo(String orderNo);
}
