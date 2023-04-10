package com.zrg.rtspdemo.ws;

import lombok.Data;

@Data
public class BaseResponse {
    private String code;
    private String data;
    private String message;
}
