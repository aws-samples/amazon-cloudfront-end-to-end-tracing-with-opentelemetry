package com.aws.peach.application.order;

import com.aws.peach.application.order.dto.OrderDTO;
import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.repository.OrderCacheRepository;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderViewService {

    private final OrderRepository orderRepository;
    private final OrderCacheRepository orderCacheRepository;

    public Optional<OrderDTO> getOrder(final OrderNumber orderNo) {
        Optional<Orders> entity = this.orderCacheRepository.findOne(orderNo);
        if (entity.isPresent()) {
            return entity.map(OrderDTO::newInstance);
        }
        entity = this.orderRepository.findById(orderNo);
        entity.ifPresent(this.orderCacheRepository::save);
        return entity.map(OrderDTO::newInstance);
    }
}
