package com.aws.peach.application;

import com.aws.peach.domain.delivery.*;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeliveryTestRepository implements DeliveryRepository {

    private final Map<String, Delivery> orderNo2Delivery;
    private final Map<Long, Delivery> deliveryId2Delivery;

    public DeliveryTestRepository() {
        this.orderNo2Delivery = new ConcurrentHashMap<>();
        this.deliveryId2Delivery = new ConcurrentHashMap<>();
    }

    DeliveryTestRepository(Collection<Delivery> initData) {
        this.orderNo2Delivery = initData.stream()
                .collect(Collectors.toMap(Delivery::getOrderNo, Function.identity()));
        this.deliveryId2Delivery = initData.stream()
                .collect(Collectors.toMap(Delivery::getId, Function.identity()));
    }

    @Override
    public Optional<Delivery> findById(Long deliveryId) {
        return Optional.ofNullable(this.deliveryId2Delivery.get(deliveryId));
    }

    @Override
    public Delivery save(Delivery delivery) {
//        delivery.assignNewId();
        orderNo2Delivery.put(delivery.getOrderNo(), delivery);
        deliveryId2Delivery.put(delivery.getId(), delivery);
        return delivery;
    }

    @Override
    public List<Delivery> findAll(Pageable pageable) {
        List<Delivery> all = new ArrayList<>(orderNo2Delivery.values());
        return DeliveryTestRepository.createSubList(all, pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public List<Delivery> findAllByStatus(DeliveryStatus.Type type, Pageable pageable) {
        List<Delivery> filtered = orderNo2Delivery.values().stream()
                .filter(d -> d.getStatus().getType().equals(type))
                .collect(Collectors.toList());
        return DeliveryTestRepository.createSubList(filtered, pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public Optional<Delivery> findByOrderNo(String orderNo) {
        return Optional.ofNullable(this.orderNo2Delivery.get(orderNo));
    }

    private static List<Delivery> createSubList(List<Delivery> originalList, int pageNo, int pageSize) {
        if (originalList.isEmpty()) {
            return Collections.emptyList();
        }
        final int from = Math.max(pageNo * pageSize, 0);
        final int to = Math.min(from + pageSize, originalList.size());
        return originalList.subList(from, to);
    }
}
