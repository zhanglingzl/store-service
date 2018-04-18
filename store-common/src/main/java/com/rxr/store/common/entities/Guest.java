package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "rxr_guest")
public class Guest extends BaseEntity{

    @Column(name = "gu_name")
    private String name;
    /**性别*/
    @Column(name = "gu_gender")
    private String gender;
    @Column(name = "gu_telephone")
    private String telephone;
    @Column(name = "gu_email")
    private String email;
    @Column(name = "gu_photo")
    private String photo;
    @Column(name = "gu_cardNo")
    private String cardNo;
    @Column(name = "gu_address_id")
    private String addressId;
    @Column(name = "gu_wechat_no")
    private String weChatNo;

}
