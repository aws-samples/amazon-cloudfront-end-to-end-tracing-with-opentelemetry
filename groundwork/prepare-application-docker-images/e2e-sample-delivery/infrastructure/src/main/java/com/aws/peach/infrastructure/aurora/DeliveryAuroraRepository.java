package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryAuroraRepository extends DeliveryRepository, JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d WHERE d.order.orderNo = ?1")
    Optional<Delivery> findByOrderNo(String orderNo);
}
