package com.rxr.store.web.common;

import lombok.Data;

@Data
public class RestResponse<T> {
    private int    code;
    private String msg;
    private T      result;

    public static <T> RestResponse<T> success() {
        return new RestResponse<>();
    }

    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        return response;
    }

    public static <T> RestResponse<T> error(RestCode code) {
        return new RestResponse<>(code.code,code.msg);
    }

    public RestResponse(){
        this(RestCode.OK.code, RestCode.OK.msg);
    }

    public RestResponse(int code,String msg){
        this.code = code;
        this.msg  = msg;
    }
}
