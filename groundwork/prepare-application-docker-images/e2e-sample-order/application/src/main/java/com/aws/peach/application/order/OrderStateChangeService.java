package com.aws.peach.application.order;

import com.aws.peach.domain.delivery.DeliveryChangeMessage;
import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.repository.OrderCacheRepository;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStateChangeService {
    private final OrderRepository orderRepository;
    private final OrderCacheRepository orderCacheRepository;

    @Transactional
    public void changeOrderState(DeliveryChangeMessage message) {
        if (message.isShipped()) {
            OrderNumber orderNumber = new OrderNumber(message.getOrderNo());
            closeOrder(orderNumber);
        }
    }

    private void closeOrder(OrderNumber orderNumber) {
        Optional<Orders> order = orderRepository.findById(orderNumber);
        order.ifPresent(o -> {
            o.close();
            orderCacheRepository.save(o);
        });
    }
}
