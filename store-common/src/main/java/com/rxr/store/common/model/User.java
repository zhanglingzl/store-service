package com.rxr.store.common.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "rxr_user")
public class User extends BaseEntity{
    @Column(name = "user_login_name", unique = true)
    private String loginName;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_salt")
    private String salt;
    @Column(name = "user_avatar")
    private String avatar;
    @Column(name = "user_telephone", unique = true)
    private String telephone;
    @Column(name = "user_wechat", unique = true)
    private String wechat;
    @Column(name = "user_state")
    private Integer state;

    @ManyToMany
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = " role_id")}
    )
    private List<Role> roles;
}
