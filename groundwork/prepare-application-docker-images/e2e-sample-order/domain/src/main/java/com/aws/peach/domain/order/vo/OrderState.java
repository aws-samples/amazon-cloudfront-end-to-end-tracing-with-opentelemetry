package com.aws.peach.domain.order.vo;

public enum OrderState {
    PLACED, CLOSED;

    public static OrderState findByName(String name) {
        for (OrderState state : OrderState.values()) {
            if (state.name().equals(name)) {
                return state;
            }
        }
        return null;
    }
}
