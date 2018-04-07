package com.rxr.store.common.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private String remark;
}
