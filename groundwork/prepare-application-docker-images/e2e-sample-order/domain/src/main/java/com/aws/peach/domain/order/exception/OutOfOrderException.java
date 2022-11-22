package com.aws.peach.domain.order.exception;

public class OutOfOrderException extends OrderException {
    @Override
    public String getMessage() {
        //TODO: message code 적용
        return "재고가 초과되어 주문에 실패하였습니다. 내일 주문 해주세요.";
    }
}
