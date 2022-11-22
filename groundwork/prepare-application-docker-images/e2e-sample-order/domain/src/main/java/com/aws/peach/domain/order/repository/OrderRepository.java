package com.aws.peach.domain.order.repository;

import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.vo.OrderNumber;

import java.util.Optional;

public interface OrderRepository {
    Orders save(final Orders order);
    Optional<Orders> findById(OrderNumber orderNumber);
    OrderNumber nextOrderNo();
}
