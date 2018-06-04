package com.rxr.store.wechat.model;

import lombok.Data;

import java.util.List;

@Data
public class WechatInfo {
    private Integer subscribe;
    private String openid;
    private String nickname;
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private Long subscribeTime;
    private String unionid;
    private String remark;
    private String groupid;
    private List<Integer> tagidList;
    private String subscribeScene;
}
