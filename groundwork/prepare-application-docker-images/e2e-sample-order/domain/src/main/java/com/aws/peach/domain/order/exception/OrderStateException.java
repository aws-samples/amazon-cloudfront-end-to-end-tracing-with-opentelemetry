package com.aws.peach.domain.order.exception;

public class OrderStateException extends OrderException {
    @Override
    public String getMessage() {
        // TODO: 메시지 상세화하기
        return "주문 상태가 잘못되었습니다.";
    }
}
