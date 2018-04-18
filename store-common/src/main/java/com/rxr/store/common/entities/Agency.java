package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@ToString
@Table(name = "rxr_agency")
public class Agency extends BaseEntity{

    @Column(name = "ag_type")
    private Integer type;
    @Column(name = "ag_telephone")
    private String telephone;
    @Column(name = "ag_email")
    private String email;
    @Column(name = "ag_photo")
    private String photo;
    @Column(name = "ag_cardNo")
    private String cardNo;
    @Column(name = "ag_address_id")
    private String addressId;
    @Column(name = "ag_company")
    private String company;
    /**性别*/
    @Column(name = "ag_gender")
    private String gender;
    @Column(name = "ag_name")
    private String name;


}
