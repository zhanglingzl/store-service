package com.rxr.store.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "rxr_address")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties("agency")
public class Address extends BaseEntity {

    private String name;
    private String phone;
    private String province;
    private String city;
    /**区*/
    private String country;
    private String cityCode;
    /**详细地址*/
    private String address;
    /**是否是默认地址 0:否,1:是*/
    private Integer status;
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

}
