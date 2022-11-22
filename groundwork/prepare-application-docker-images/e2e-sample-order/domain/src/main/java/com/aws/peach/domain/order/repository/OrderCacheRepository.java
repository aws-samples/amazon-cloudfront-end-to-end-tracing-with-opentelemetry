package com.aws.peach.domain.order.repository;

import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.vo.OrderNumber;

import java.util.Optional;

public interface OrderCacheRepository {
    void save(Orders order);

    Optional<Orders> findOne(OrderNumber orderNo);
}
