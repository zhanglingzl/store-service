package com.rxr.store.common.form;

import lombok.Data;

/**
 * @author zero
 * @date Create in 2018/6/2 14:43
 */
@Data
public class AgencyForm {

    private Long id;
    /**代理类型 1: 一级代理, 2: 二级代理, 3: 三级代理*/
    private Integer type;
    private String name;
    private String wechat;
    private String telephone;
    private String email;
    private Long parentId;
}
