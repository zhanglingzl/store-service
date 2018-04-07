package com.rxr.store.common.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;


@Entity
public class Agency extends BaseEntity{
    private Integer type;
    private String telephone;
    private String email;
    private String photo;
    private String idNo;
    private String addrId;
    private String company;
    //性别
    private String gender;
    private String name;


}
