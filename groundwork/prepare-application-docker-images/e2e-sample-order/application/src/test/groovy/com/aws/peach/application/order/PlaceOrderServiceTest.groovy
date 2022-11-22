package com.aws.peach.application.order


import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.order.entity.Orders
import com.aws.peach.domain.order.repository.OrderCacheRepository
import com.aws.peach.domain.order.repository.OrderRepository
import com.aws.peach.domain.order.vo.OrderNumber
import com.aws.peach.domain.order.vo.OrderState
import com.aws.peach.domain.order.vo.ShippingInformation
import com.aws.peach.domain.product.entity.Product
import com.aws.peach.domain.product.repository.ProductRepository
import com.google.common.collect.Lists
import spock.lang.Specification

class PlaceOrderServiceTest extends Specification {

    def "place order happy case"() {
        given:
        OrderRepository orderRepository = stubOrderRepository()
        ProductRepository productRepository = stubProductRepository()
        DeliveryService deliveryService = stubDeliveryService()
        OrderCacheRepository orderCacheRepository = Mock()
        PlaceOrderService placeOrderService = new PlaceOrderService(orderRepository,  productRepository,  deliveryService, orderCacheRepository)
        PlaceOrderService.ShippingRequest shippingRequest = createShippingRequest()
        PlaceOrderService.PlaceOrderRequest request = createRequest()
        // for saved argument capture
        Orders savedOrder

        when:
        String orderNo = placeOrderService.placeOrder(request)

        then:
        1 * orderRepository.save(_ as Orders) >> { arguments -> savedOrder=arguments[0]}
        savedOrder instanceof Orders
        savedOrder.getOrderNumber() == orderNo
        savedOrder.getOrderState() == OrderState.PLACED
        ShippingInformation savedShippingInformation = savedOrder.getShippingInformation()
        savedShippingInformation.city == request.getShippingRequest().getCity()
        savedShippingInformation.receiver == request.getShippingRequest().getReceiver()
        savedShippingInformation.telephoneNumber == request.getShippingRequest().getTelephoneNumber()
        savedShippingInformation.address1 == request.getShippingRequest().getAddress1()
        savedShippingInformation.address2 == request.getShippingRequest().getAddress2()
    }

    private OrderRepository stubOrderRepository() {
        OrderRepository orderRepository = Spy()
        1 * orderRepository.nextOrderNo() >> OrderNumber.builder().orderNumber("ORDER-1").build()
        return orderRepository
    }

    private ProductRepository stubProductRepository() {
        ProductRepository productRepository = Stub()
        productRepository.findByIdIn(_ as List<String>) >> Lists.asList(
                Product.builder()
                        .id("GOLD-PEACH-1")
                        .name("Gold Peach #1")
                        .price(3_000)
                        .build()
        )
        return productRepository
    }

    private DeliveryService stubDeliveryService() {
        DeliveryService deliveryService = Stub()
        deliveryService.placeDeliveryOrder() >> new DeliveryId("delivery-id")
        return deliveryService
    }

    private PlaceOrderService.PlaceOrderRequest createRequest() {
        return PlaceOrderService.PlaceOrderRequest.builder()
                .orderer("Alice")
                .orderLines(
                        Lists.asList(
                                PlaceOrderService.OrderRequestLine.of("GOLD-PEACH-1", 3)
                        ))
                .shippingRequest(createShippingRequest())
                .build()
    }

    private PlaceOrderService.ShippingRequest createShippingRequest() {
        return PlaceOrderService.ShippingRequest.builder()
                .city("Seoul")
                .telephoneNumber("01012341234")
                .receiver("Bob")
                .address1("Spruce Street 101")
                .build()
    }
}
