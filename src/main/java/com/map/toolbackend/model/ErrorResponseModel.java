package com.map.toolbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorResponseModel extends BaseResponseModel {
    private String error;

    public ErrorResponseModel(int response_code, String error, String message) {
        super.setSuccess(false);
        super.setResponse_code(response_code);
        super.setMessage(message);
        this.error = error;
    }
}