package com.map.toolbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseModel extends BaseResponseModel {
    private Object payload;

    public ResponseModel(int response_code, String message, Object payload) {
        super.setSuccess(true);
        super.setResponse_code(response_code);
        super.setMessage(message);
        this.payload = payload;
    }
}