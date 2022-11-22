package com.aws.peach.application.order;

import com.aws.peach.application.order.dto.OrderDTO;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.repository.OrderCacheRepository;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.*;
import com.aws.peach.domain.product.Products;
import com.aws.peach.domain.product.repository.ProductRepository;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaceOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DeliveryService deliveryService;
    private final OrderCacheRepository orderCacheRepository;

    @Transactional
    public String placeOrder(final PlaceOrderRequest request) {
        Orders order = createOrder(request);

        DeliveryId deliveryId = deliveryService.placeDeliveryOrder(OrderDTO.newInstance(order));
        if (deliveryId != null) {
            order.updateDeliveryId(deliveryId);
        }

        orderCacheRepository.save(order);

        return order.getOrderNumber();
    }

    private Orders createOrder(final PlaceOrderRequest request) {
        final Products products = Products.create(this.productRepository.findAllById(request.getProductIds()));
        final OrderNumber orderNumber = this.orderRepository.nextOrderNo();

        final List<OrderLine> orderLines = request.getOrderLines().stream()
                .map(m -> OrderLine.builder()
                        .quantity(m.getQuantity())
                        .orderProduct(OrderProduct.builder()
                                .productId(m.getProductId())
                                .productName(products.getProductName(m.getProductId()))
                                .price(products.getProductPrice(m.getProductId()))
                                .build())
                        .build())
                .collect(Collectors.toList());

        Orders order =  Orders.builder()
                .orderNumber(orderNumber)
                .customerId(request.getOrderer())
                .orderLines(orderLines)
                .orderState(OrderState.PLACED)
                .orderDateTime(Instant.now())
                .shippingInformation(makeShippingInformationFrom(request.getShippingRequest()))
                .build();

        return orderRepository.save(order);
    }

    private ShippingInformation makeShippingInformationFrom(ShippingRequest request) {
        return ShippingInformation.builder()
                .city(request.getCity())
                .telephoneNumber(request.getTelephoneNumber())
                .receiver(request.getReceiver())
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PlaceOrderRequest {
        private String orderer;                       // 주문자 (회원아이디)
        private List<OrderRequestLine> orderLines;    // 주문상품과 배송지 정보
        private ShippingRequest shippingRequest;

        public List<String> getProductIds() {
            return this.orderLines.stream().map(OrderRequestLine::getProductId).collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OrderRequestLine {
        private String productId;
        private int quantity;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ShippingRequest {
        private String city;
        private String telephoneNumber;
        private String receiver;
        private String address1;
        private String address2;
    }

}
