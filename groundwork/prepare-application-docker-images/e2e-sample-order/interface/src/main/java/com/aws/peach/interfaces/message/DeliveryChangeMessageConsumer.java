package com.aws.peach.interfaces.message;

import com.aws.peach.application.order.OrderStateChangeService;
import com.aws.peach.domain.delivery.DeliveryChangeMessage;
import com.aws.peach.domain.support.Message;
import com.aws.peach.domain.support.MessageConsumer;
import org.springframework.stereotype.Component;

@Component
public class DeliveryChangeMessageConsumer implements MessageConsumer<DeliveryChangeMessage> {
    private final OrderStateChangeService orderStateChangeService;

    public DeliveryChangeMessageConsumer(final OrderStateChangeService orderStateChangeService) {
        this.orderStateChangeService = orderStateChangeService;
    }
    @Override
    public void consume(Message<DeliveryChangeMessage> message) {
        orderStateChangeService.changeOrderState(message.getPayload());
    }
}
