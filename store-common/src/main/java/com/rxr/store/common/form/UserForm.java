package com.rxr.store.common.form;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class UserForm {
    private Date startTime;
    private Date endTime;
    private Long id;
    private String LoginName;
    private String name;
    private String wechat;
    private String telephone;
    private Integer state;
}
