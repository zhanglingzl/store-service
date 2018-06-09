package com.rxr.store.wechat.model;

import lombok.Data;
import lombok.ToString;

/**
 * 微信返回信息封装
 */
@Data
@ToString
public class Message {
    private int errcode;
    private String errmsg;
}
