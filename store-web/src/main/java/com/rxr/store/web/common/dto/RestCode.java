package com.rxr.store.web.common.dto;

public enum  RestCode {
    OK(0,"ok"),
    UNKNOWN_ERROR(500,"未知异常"),
    NOT_FOUND(404,"访问的资源不存在");

    public final int code;
    public final String msg;

    RestCode(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
