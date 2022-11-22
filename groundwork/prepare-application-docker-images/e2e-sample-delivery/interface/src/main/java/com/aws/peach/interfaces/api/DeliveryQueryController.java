package com.aws.peach.interfaces.api;

import com.aws.peach.application.DeliveryQueryService;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.application.dto.DeliveryDetailResponse;
import com.aws.peach.interfaces.api.model.DeliverySearchRequest;
import com.aws.peach.application.dto.DeliverySearchResponse;
import com.aws.peach.interfaces.common.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryQueryController {
    private final DeliveryQueryService deliveryQueryService;

    public DeliveryQueryController(DeliveryQueryService deliveryQueryService) {
        this.deliveryQueryService = deliveryQueryService;
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetailResponse> queryById(@PathVariable String deliveryId) {
        log.info("GET /delivery/{}", deliveryId);
        return deliveryQueryService.getDelivery(new DeliveryId(Long.parseLong(deliveryId)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping
    public ResponseEntity<DeliveryDetailResponse> queryByOrderNo(@RequestParam(name = "orderNo") String orderNo) {
        log.info("GET /delivery?orderNo={}", orderNo);
        return deliveryQueryService.getDelivery(orderNo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping("/searches")
    public ResponseEntity<DeliverySearchResponse> search(@Valid DeliverySearchRequest request,
                                                         BindingResult bindingResult) {
        log.info("GET /delivery/searches?pageNo={}&pageSize={}&state={}",
                request.getPageNo(), request.getPageSize(), request.getState());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        DeliveryQueryService.SearchResult queryResult = deliveryQueryService.search(
                new DeliveryQueryService.SearchCondition(request.getPageNo(), request.getPageSize(), request.getState()));
        DeliverySearchResponse response = DeliverySearchResponse.of(queryResult);
        return ResponseEntity.ok(response);
    }
}
