package com.map.toolbackend.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class BaseResponseModel {
    private boolean success;
    private int response_code;
    private String message;
    private String timestamp = new Timestamp(System.currentTimeMillis()).toString();
}
