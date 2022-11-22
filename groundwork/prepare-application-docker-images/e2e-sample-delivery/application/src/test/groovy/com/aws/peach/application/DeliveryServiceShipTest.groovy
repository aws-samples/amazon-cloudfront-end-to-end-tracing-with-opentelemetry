package com.aws.peach.application

import com.aws.peach.application.dto.DeliveryDetailResponse
import com.aws.peach.domain.delivery.Address
import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryChangeMessage
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.DeliveryStatus
import com.aws.peach.domain.delivery.Order
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException
import com.aws.peach.domain.support.MessageProducer
import spock.lang.Specification

class DeliveryServiceShipTest extends Specification {

    Long deliveryId = 123L
    String orderNo = "o123"
    MessageProducer<String, DeliveryChangeMessage> messageProducer

    def setup() {
        messageProducer = Mock()
    }

    def stubDeliveryRepository(Long deliveryId, Delivery retrievedDelivery) {
        DeliveryRepository repository = Stub()
        repository.findById(deliveryId) >> Optional.ofNullable(retrievedDelivery)
        return repository
    }

    def "if delivery not found, throw error"() {
        given:
        Delivery retrievedDelivery = null
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer)

        when:
        service.ship(new DeliveryId(deliveryId))

        then:
        thrown(DeliveryNotFoundException.class)
    }

    def "upon success, mark delivery order as 'SHIPPED'"() {
        given:
        Delivery retrievedDelivery = mockRetrievedDelivery(deliveryId, orderNo, DeliveryStatus.Type.PREPARING)
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer)

        when:
        DeliveryDetailResponse result = service.ship(new DeliveryId(deliveryId))

        then:
        1 * retrievedDelivery.ship()
        1 * messageProducer.send(String.valueOf(deliveryId), _ as DeliveryChangeMessage)
    }

    Delivery mockRetrievedDelivery(Long deliveryId, String orderNo, DeliveryStatus.Type statusType) {
        Order.Orderer retrievedOrderer = Mock()
        Order retrievedOrder= Mock()
        retrievedOrder.getOrderer() >> retrievedOrderer

        Address shipping = Mock()
        Address sending = Mock()

        Delivery retrievedDelivery = Mock()
        retrievedDelivery.getIdString() >> String.valueOf(deliveryId)
        retrievedDelivery.getOrder() >> retrievedOrder
        retrievedDelivery.getOrderNo() >> orderNo
        retrievedDelivery.getStatus() >> new DeliveryStatus(statusType)
        retrievedDelivery.getItems() >> Collections.emptyList()
        retrievedDelivery.getSender() >> sending
        retrievedDelivery.getReceiver() >> shipping
        return retrievedDelivery
    }
}
