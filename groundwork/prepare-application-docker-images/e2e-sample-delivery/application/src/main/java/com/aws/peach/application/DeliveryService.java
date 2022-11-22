package com.aws.peach.application;

import com.aws.peach.application.dto.DeliveryDetailResponse;
import com.aws.peach.domain.delivery.*;
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException;
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException;
import com.aws.peach.domain.support.MessageProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class DeliveryService {

    private final DeliveryRepository repository;
    private final MessageProducer<String, DeliveryChangeMessage> messageProducer;

    public DeliveryService(final DeliveryRepository repository,
                           final MessageProducer<String, DeliveryChangeMessage> messageProducer) {
        this.repository = repository;
        this.messageProducer = messageProducer;
    }

    public DeliveryDetailResponse createDeliveryOrder(CreateDeliveryInput input) {
        Delivery delivery = CreateDeliveryInput.newDelivery(input, getSenderAddress(input.getOrder().getOrdererName()));
        Optional<Delivery> existingDelivery = repository.findByOrderNo(delivery.getOrderNo());
        if (existingDelivery.isPresent()) {
            throw new DeliveryAlreadyExistsException(new DeliveryId(existingDelivery.get().getId()));
        }
        return DeliveryDetailResponse.of(repository.save(delivery));
    }

    private Address getSenderAddress(String ordererName) {// TODO: another service
        return Address.builder()
                .name(ordererName)
                .telephone("010-1111-2222")
                .city("Blue Mountain")
                .address1("Pine Valley 123")
                .build();
    }

    @Transactional
    public DeliveryDetailResponse ship(final DeliveryId deliveryId) {
        Delivery delivery = updateDeliveryStatus(deliveryId, Delivery::ship);
        publishMessage(delivery);
        return DeliveryDetailResponse.of(delivery);
    }

    private Delivery updateDeliveryStatus(final DeliveryId deliveryId, final Consumer<Delivery> updater) {
        Optional<Delivery> delivery = repository.findById(deliveryId.getValue());
        delivery.ifPresent(delivery1 -> {
            updater.accept(delivery1);
            repository.save(delivery1);
        });
        return delivery.orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }

    private void publishMessage(Delivery delivery) {
        DeliveryChangeMessage message = DeliveryChangeMessage.of(delivery);
        messageProducer.send(String.valueOf(message.getDeliveryId()), message);
    }
}
