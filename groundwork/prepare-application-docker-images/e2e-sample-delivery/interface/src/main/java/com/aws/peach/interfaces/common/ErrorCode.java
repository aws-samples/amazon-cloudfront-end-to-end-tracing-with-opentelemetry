package com.aws.peach.interfaces.common;

public enum ErrorCode {
    // 요청 메세지 관련
    INVALID_MESSAGE_FORMAT(1100, "message.format"),
    INVALID_REQUEST(1101, "message.contents"),

    // 배송처리 비즈니스 로직 관련
    DELIVERY_DUPLICATE(1200, "delivery.create.duplicate"),
    DELIVERY_NOT_FOUND(1201, "delivery.not.found"),
    DELIVERY_ILLEGAL_STATE(1202, "delivery.illegal.state"),

    UNDEFINED(9999, "server.undefined");

    private final int value;
    private final String desc;

    ErrorCode(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
