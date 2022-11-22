package com.aws.peach.application.order;

import com.aws.peach.application.order.dto.OrderDTO;
import com.aws.peach.domain.delivery.Address;
import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.delivery.DeliveryRepository;
import com.aws.peach.domain.order.vo.OrderNumber;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    private final DeliveryRepository repository;

    public DeliveryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public DeliveryId placeDeliveryOrder(OrderDTO order) {
        Delivery delivery = convertToDelivery(order);
        return repository.create(delivery);
    }

    private Delivery convertToDelivery(OrderDTO order) {
        List<Delivery.Item> items = order.getOrderLines().stream()
                .map(ol -> new Delivery.Item(ol.getProductId(), ol.getProductName(), ol.getQuantity()))
                .collect(Collectors.toList());

        Address shipTo = Address.builder()
                .name(order.getShippingInfo().getReceiver())
                .city(order.getShippingInfo().getCity())
                .telephone(order.getShippingInfo().getTelephone())
                .address1(order.getShippingInfo().getAddress1())
                .address2(order.getShippingInfo().getAddress2())
                .build();

        return Delivery.builder()
                .orderNumber(new OrderNumber(order.getOrderNumber()))
                .ordererId(order.getOrdererId())
                .ordererName(order.getOrdererName())
                .items(items)
                .orderCreatedDateTime(order.getCreatedDateTime())
                .shipToAddress(shipTo)
                .build();
    }
}
