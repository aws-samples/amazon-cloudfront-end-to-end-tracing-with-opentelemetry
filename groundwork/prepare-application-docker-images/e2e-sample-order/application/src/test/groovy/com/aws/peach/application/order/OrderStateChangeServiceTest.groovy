package com.aws.peach.application.order

import com.aws.peach.domain.delivery.DeliveryChangeMessage
import com.aws.peach.domain.order.entity.Orders
import com.aws.peach.domain.order.repository.OrderCacheRepository
import com.aws.peach.domain.order.repository.OrderRepository
import com.aws.peach.domain.order.vo.OrderNumber
import com.aws.peach.domain.order.vo.OrderState
import spock.lang.Specification

class OrderStateChangeServiceTest extends Specification {

    String orderId = "ORDER-1"
    OrderRepository repository
    OrderCacheRepository orderCacheRepository
    OrderStateChangeService service

    def setup() {
        repository = Mock()
        orderCacheRepository = Mock()
        service = new OrderStateChangeService(repository, orderCacheRepository)
    }

    def "change order state in response to delivery message"(OrderState oldOrderState,
                                                           DeliveryChangeMessage.Status deliveryStatus,
                                                           OrderState newOrderState) {
        given:
        Optional<Orders> order = createOrder(orderId, oldOrderState)
        DeliveryChangeMessage message = createDeliveryMessage(orderId, deliveryStatus)

        when:
        service.changeOrderState(message)

        then:
        1 * repository.findById(new OrderNumber(orderId)) >> order
        order.get().orderState == newOrderState

        where:
        oldOrderState           |   deliveryStatus                         |   newOrderState
        OrderState.PLACED       |   DeliveryChangeMessage.Status.SHIPPED   |   OrderState.CLOSED
    }

    static Optional<Orders> createOrder(String orderId, OrderState orderState) {
        def order =  Orders.builder()
                .orderNumber(OrderNumber.builder().orderNumber(orderId).build())
                .orderState(orderState)
                .build()
        return Optional.of(order)
    }

    static DeliveryChangeMessage createDeliveryMessage(String orderId, DeliveryChangeMessage.Status deliveryStatus) {
        return DeliveryChangeMessage.builder()
                .orderNo(orderId)
                .status(deliveryStatus.name())
                .build()
    }
}
