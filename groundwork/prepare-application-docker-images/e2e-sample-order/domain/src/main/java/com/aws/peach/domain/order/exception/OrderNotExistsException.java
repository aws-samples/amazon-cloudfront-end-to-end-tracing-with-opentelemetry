package com.aws.peach.domain.order.exception;

public class OrderNotExistsException extends OrderException {

    private final String orderNumber;

    public OrderNotExistsException(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String getMessage() {
        return String.format("주문번호 %s 건의 주문 정보가 존재하지 않습니다", orderNumber);
    }
}
