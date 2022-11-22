package com.aws.peach.interfaces.api.model;

import com.aws.peach.interfaces.common.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int errorCode;
    private final String errorCodeDesc;
    private final String errorMsg;

    public ErrorResponse(ErrorCode errorCode, String errorMsg) {
        this.errorCode = errorCode.getValue();
        this.errorCodeDesc = errorCode.getDesc();
        this.errorMsg = errorMsg;
    }
}
