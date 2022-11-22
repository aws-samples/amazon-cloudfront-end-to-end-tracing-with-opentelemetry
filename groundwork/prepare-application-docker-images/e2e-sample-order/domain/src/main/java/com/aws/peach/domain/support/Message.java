package com.aws.peach.domain.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class Message<V> {
    private final Id messageId;
    private final String messageKey;
    private final V payload;

    @RequiredArgsConstructor
    @Getter
    public static class Id {
        private final String value;
    }
}
