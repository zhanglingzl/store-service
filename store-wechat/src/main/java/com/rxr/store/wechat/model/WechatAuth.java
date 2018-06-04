package com.rxr.store.wechat.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WechatAuth {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}
