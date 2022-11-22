package com.aws.peach.infrastructure.redis;

import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.repository.OrderCacheRepository;
import com.aws.peach.domain.order.vo.*;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class OrderCacheRedisRepository implements OrderCacheRepository {

    private static final String KEY_PREFIX = "Order:";
    private final RedisTemplate<String, OrderData> template;

    public OrderCacheRedisRepository(RedisTemplate<String, OrderData> template) {
        this.template = template;
    }

    @Override
    public void save(Orders orders) {
        final String key = getCacheKey(orders.getOrderNumber());
        final OrderData value = convertToCacheValue(orders);
        BoundValueOperations<String, OrderData> ops = template.boundValueOps(key);
        ops.set(value, 1, TimeUnit.MINUTES);
    }

    @Override
    public Optional<Orders> findOne(OrderNumber orderNo) {
        final String key = getCacheKey(orderNo.getOrderNumber());
        BoundValueOperations<String, OrderData> ops = template.boundValueOps(key);
        OrderData value = ops.get();
        return Optional.ofNullable(convertFromCacheValue(value));
    }

    private String getCacheKey(String orderNumber) {
        return KEY_PREFIX + orderNumber;
    }

    private OrderData convertToCacheValue(Orders orders) {
        List<OrderData.Item> items = orders.getOrderLines().stream()
                .map(line -> {
                    OrderData.Item item = new OrderData.Item();
                    item.setId(line.getId());
                    item.setPrdId(line.getOrderProduct().getProductId());
                    item.setPrdNm(line.getOrderProduct().getProductName());
                    item.setPrdPrice(line.getOrderProduct().getPrice());
                    item.setQty(line.getQuantity());
                    return item;
                })
                .collect(Collectors.toList());

        OrderData.Address shipTo = new OrderData.Address();
        shipTo.setCity(orders.getShippingInformation().getCity());
        shipTo.setTel(orders.getShippingInformation().getTelephoneNumber());
        shipTo.setName(orders.getShippingInformation().getReceiver());
        shipTo.setAddr1(orders.getShippingInformation().getAddress1());
        shipTo.setAddr2(orders.getShippingInformation().getAddress2());

        OrderData data = new OrderData();
        data.setOrderNo(orders.getOrderNumber());
        data.setDelId(orders.getDeliveryId());
        data.setItems(items);
        data.setCustId(orders.getCustomerId());
        data.setCustNm(orders.getCustomerName());
        data.setStatus(orders.getOrderState().name());
        data.setCrtAt(orders.getOrderDateTime());
        data.setShipTo(shipTo);
        return data;
    }

    private Orders convertFromCacheValue(OrderData data) {
        if (data == null) {
            return null;
        }
        final OrderNumber orderNumber = new OrderNumber(data.getOrderNo());
        List<OrderLine> orderLines = data.getItems().stream()
                .map(item -> {
                    OrderProduct product = OrderProduct.builder()
                            .productId(item.getPrdId())
                            .productName(item.getPrdNm())
                            .price(item.getPrdPrice())
                            .build();
                    return OrderLine.builder()
                            .id(item.getId())
                            .orderNumber(orderNumber)
                            .orderProduct(product)
                            .quantity(item.getQty())
                            .build();
                })
                .collect(Collectors.toList());
        ShippingInformation shippingInformation = ShippingInformation.builder()
                .city(data.getShipTo().getCity())
                .telephoneNumber(data.getShipTo().getTel())
                .receiver(data.getShipTo().getName())
                .address1(data.getShipTo().getAddr1())
                .address2(data.getShipTo().getAddr2())
                .build();

        Orders orders = Orders.builder()
                .orderNumber(orderNumber)
                .deliveryId(data.getDelId())
                .orderLines(orderLines)
                .customerId(data.getCustId())
                .customerName(data.getCustNm())
                .orderState(OrderState.findByName(data.getStatus()))
                .orderDateTime(data.getCrtAt())
                .shippingInformation(shippingInformation)
                .build();
        return orders;
    }
}
