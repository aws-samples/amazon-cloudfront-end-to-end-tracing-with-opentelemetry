package com.aws.peach.application

import com.aws.peach.application.dto.DeliveryDetailResponse
import com.aws.peach.domain.delivery.Address
import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryChangeMessage
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.Order
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException
import com.aws.peach.domain.support.MessageProducer
import spock.lang.Specification

import java.time.Instant

class DeliveryServiceReceiveTest extends Specification {

    // test data
    String orderNo = "oid"
    MessageProducer<String, DeliveryChangeMessage> messageProducer
    CreateDeliveryInput createInput

    def setup() {
        messageProducer = Mock()

        Instant orderCreatedAt = Instant.now()
        String ordererId = "1"
        String ordererName = "PeachMan"
        List<CreateDeliveryInput.OrderProductDto> orderProducts = Arrays.asList(
                CreateDeliveryInput.OrderProductDto.builder().name("YP").quantity(1).build(),
                CreateDeliveryInput.OrderProductDto.builder().name("WP").quantity(2).build()
        )

        def orderDto = CreateDeliveryInput.OrderDto.builder()
                .id(orderNo)
                .createdAt(orderCreatedAt)
                .ordererId(ordererId)
                .ordererName(ordererName)
                .products(orderProducts)
                .build()
        def receiver = Address.builder()
                .name("Sandy")
                .telephone("010-1234-1234")
                .city("Seoul")
                .address1("Teheran-ro 100")
                .address2("Royal Building 23rd floor")
                .build()
        createInput = CreateDeliveryInput.builder()
                    .order(orderDto)
                    .receiver(receiver).build()
    }

    def "if delivery exists, throw error"() {
        given:
        Delivery existingDelivery = fakeExistingDelivery(orderNo)
        DeliveryRepository repository = createTestRepository(existingDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer)

        when:
        service.createDeliveryOrder(createInput)

        then:
        thrown(DeliveryAlreadyExistsException.class)
    }

    def "upon success, save delivery order as 'PREPARING'"() {
        given:
        Delivery existingDelivery = null
        DeliveryRepository repository = createTestRepository(existingDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer)

        when:
        DeliveryDetailResponse result = service.createDeliveryOrder(createInput)

        then:
        result.order.orderNo == createInput.order.id
        result.order.openedAt == createInput.order.createdAt.toString()
        result.order.ordererId == createInput.order.ordererId
        result.order.ordererName == createInput.order.ordererName
        // TODO test sender parsing
        result.shippingAddress.name == createInput.receiver.name
        result.shippingAddress.city == createInput.receiver.city
        result.shippingAddress.telephone == createInput.receiver.telephone
        result.shippingAddress.address1 == createInput.receiver.address1
        result.shippingAddress.address2 == createInput.receiver.address2
        result.items.get(0).name == createInput.order.products.get(0).name
        result.items.get(0).quantity == createInput.order.products.get(0).quantity
        result.items.get(1).name == createInput.order.products.get(1).name
        result.items.get(1).quantity == createInput.order.products.get(1).quantity
    }

    private static DeliveryRepository createTestRepository(Delivery existingDelivery) {
        List<Delivery> initData = existingDelivery == null ? new ArrayList<Delivery>() : Arrays.asList(existingDelivery)
        return new DeliveryTestRepository(initData)
    }

    private static Delivery fakeExistingDelivery(String orderNo) {
        Order order = Order.builder().orderNo(orderNo).build()
        return Delivery.builder().id(123L).order(order).build()
    }
}
