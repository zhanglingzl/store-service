package com.rxr.store.wechat.model;

import lombok.Data;

@Data
public class WechatConfig {
    /** 必填，公众号的唯一标识 */
    String appId;
    /** 必填，生成签名的时间戳 */
    Long timestamp;
    /** 必填，生成签名的随机串 */
    String nonceStr;
    /** 必填，签名，见附录1 */
    String signature;

    public WechatConfig() {

    }
}
