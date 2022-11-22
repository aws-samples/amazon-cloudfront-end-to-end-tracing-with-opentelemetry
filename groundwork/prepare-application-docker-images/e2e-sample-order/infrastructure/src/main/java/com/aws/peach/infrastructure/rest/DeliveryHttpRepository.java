package com.aws.peach.infrastructure.rest;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.delivery.DeliveryRepository;
import com.aws.peach.infrastructure.rest.api.DeliveryResponse;
import com.aws.peach.infrastructure.rest.api.DeliveryServiceAPI;
import com.aws.peach.infrastructure.rest.api.DeliveryCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class DeliveryHttpRepository implements DeliveryRepository {

    private final DeliveryServiceAPI api;

    public DeliveryHttpRepository(DeliveryServiceAPI deliveryServiceAPI) {
        this.api = deliveryServiceAPI;
    }

    @Override
    public DeliveryId create(Delivery delivery) {
        DeliveryCreateRequest request = convertToRequest(delivery);
        Call<DeliveryResponse> call = api.createDeliveryOrder(request);
        try {
            retrofit2.Response<DeliveryResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return new DeliveryId(response.body().getDeliveryId());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private DeliveryCreateRequest convertToRequest(Delivery delivery) {
        List<DeliveryCreateRequest.OrderLine> orderLines = delivery.getItems().stream()
                .map(item -> {
                    DeliveryCreateRequest.OrderLine ol = new DeliveryCreateRequest.OrderLine();
                    ol.setProductName(item.getProductName());
                    ol.setQuantity(item.getQuantity());
                    return ol;
                })
                .collect(Collectors.toList());

        DeliveryCreateRequest.ShippingInfo shippingInfo = new DeliveryCreateRequest.ShippingInfo();
        shippingInfo.setCity(delivery.getShipToAddress().getCity());
        shippingInfo.setTelephoneNumber(delivery.getShipToAddress().getTelephone());
        shippingInfo.setRecipient(delivery.getShipToAddress().getName());
        shippingInfo.setAddress1(delivery.getShipToAddress().getAddress1());
        shippingInfo.setAddress2(delivery.getShipToAddress().getAddress2());

        DeliveryCreateRequest request = new DeliveryCreateRequest();
        request.setOrderNo(delivery.getOrderNumber().getOrderNumber());
        request.setOrdererId(delivery.getOrdererId());
        request.setOrdererName(delivery.getOrdererName());
        request.setOrderLines(orderLines);
        request.setOrderDate(delivery.getOrderCreatedDateTime().toString());
        request.setShippingInformation(shippingInfo);
        return request;
    }
}
