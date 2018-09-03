package com.rxr.store.common.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "rxr_agency")
@EqualsAndHashCode(callSuper = true)
public class Agency extends BaseEntity{
    /**代理类型 0: 个人,1:公司*/
    @Column(name = "ag_type")
    private Integer type;
    @Column(name = "ag_telephone")
    private String telephone;
    @Column(name = "ag_email")
    private String email;
    @Column(name = "ag_company")
    private String company;
    /**性别*/
    @Column(name = "ag_gender")
    private String gender;
    /**真实姓名*/
    @Column(name = "ag_name")
    private String name;
    @Column(name="ag_like_name")
    private String likeName;
    /**父级代理ID*/
    @Column(name = "ag_pid")
    private Long parentId;
    /**
     * 代理等级，0：普通会员，1：vip会员
     */
    @Column(name = "ag_level")
    private Integer level;
    /**头像*/
    @Column(name = "ag_avatar")
    private String avatar;
    /**微信id*/
    @Column(name = "ag_wechat_id")
    private String wechatId;
    /**微信号*/
    @Column(name="ag_wechat")
    private String wechat;
    @Column(name="ag_ticket")
    /**微信二维码ticket*/
    private String ticket;
    /**微信二维码ticket过期时间*/
    @Column(name="ag_ticket_expire")
    private Date ticketExpire;

    @OneToMany(mappedBy = "agency", fetch=FetchType.EAGER)
    private List<Answer> answers;
    @Transient
    private String token;



}
