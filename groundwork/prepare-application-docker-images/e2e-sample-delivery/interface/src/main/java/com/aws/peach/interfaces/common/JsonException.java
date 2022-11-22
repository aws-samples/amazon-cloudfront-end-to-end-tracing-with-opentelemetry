package com.aws.peach.interfaces.common;

import org.springframework.core.NestedCheckedException;

public class JsonException extends NestedCheckedException {

    public JsonException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
