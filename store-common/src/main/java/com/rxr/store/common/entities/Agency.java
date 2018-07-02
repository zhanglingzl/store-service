package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

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
    /**父级代理ID*/
    @Column(name = "ag_pid")
    private Long parentId;
    /**代理等级*/
    @Column(name = "ag_level")
    private Integer level;
    /**头像*/
    @Column(name = "ag_avatar")
    private String avatar;
    /**微信id*/
    @Column(name = "ag_wechat_id")
    private String wechatId;

    @Transient
    private String token;

    /**
     * 代理状态，0：在职代理，1：准代理,2:散户
     */
    @Column(name = "ag_status")
    private Integer status;

    @OneToMany(mappedBy = "agency", fetch=FetchType.EAGER)
    private List<Answer> answers;

    @OneToMany(mappedBy = "product_qr_code", fetch=FetchType.EAGER)
    private List<ProductQrCode> productQrCodes;

}
