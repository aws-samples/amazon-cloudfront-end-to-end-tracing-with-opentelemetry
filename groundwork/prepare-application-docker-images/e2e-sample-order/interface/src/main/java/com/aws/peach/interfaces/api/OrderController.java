package com.aws.peach.interfaces.api;

import com.aws.peach.application.order.OrderViewService;
import com.aws.peach.application.order.PlaceOrderService;
import com.aws.peach.application.order.dto.OrderDTO;
import com.aws.peach.domain.order.vo.OrderNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.aws.peach.application.order.PlaceOrderService.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final PlaceOrderService placeOrderService;
    private final OrderViewService orderViewService;

    @GetMapping
    public ResponseEntity<String> up() {
        return ResponseEntity.ok("UP");
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody PlaceOrderRequest request) {
        String orderNumber = placeOrderService.placeOrder(request);
        return ResponseEntity.ok(orderNumber);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String orderId){
        Optional<OrderDTO> order = this.orderViewService.getOrder(new OrderNumber(orderId));
        return ResponseEntity.of(order);
    }
}
