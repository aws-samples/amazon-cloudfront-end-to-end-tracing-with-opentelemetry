package com.aws.peach.interfaces.api;

import com.aws.peach.application.DeliveryService;
import com.aws.peach.application.CreateDeliveryInput;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.application.dto.DeliveryDetailResponse;
import com.aws.peach.interfaces.api.model.ReceiveDeliveryOrderRequest;
import com.aws.peach.interfaces.common.JsonException;
import com.aws.peach.interfaces.common.JsonUtil;
import com.aws.peach.interfaces.common.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final JsonUtil jsonUtil;

    public DeliveryController(DeliveryService deliveryService, JsonUtil jsonUtil) {
        this.deliveryService = deliveryService;
        this.jsonUtil = jsonUtil;
    }

    @PostMapping
    public ResponseEntity<DeliveryDetailResponse> create(@Valid @RequestBody ReceiveDeliveryOrderRequest request,
                                                         BindingResult bindingResult) {
        log.info("POST /delivery {}", serialize(request));
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        // TODO get sender information
        CreateDeliveryInput order = ReceiveDeliveryOrderRequest.newCreateDeliveryInput(request);
        return ResponseEntity.ok(deliveryService.createDeliveryOrder(order));
    }

    @PutMapping("/{deliveryId}/ship")
    public ResponseEntity<DeliveryDetailResponse> ship(@PathVariable String deliveryId) {
        log.info("PUT /delivery/{}/ship", deliveryId);
        DeliveryDetailResponse response = deliveryService.ship(new DeliveryId(Long.parseLong(deliveryId)));
        return ResponseEntity.ok(response);
    }

    private String serialize(ReceiveDeliveryOrderRequest request) {
        try {
            return jsonUtil.serialize(request);
        } catch (JsonException e) {
            return request.toString();
        }
    }
}
