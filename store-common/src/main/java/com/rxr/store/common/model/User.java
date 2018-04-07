package com.rxr.store.common.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "rxr_user")
public class User extends BaseEntity{
    @Column(unique = true)
    private String userName;
    private String name;
    private String password;
    private String salt;
    private String avatar;
    @Column(unique = true)
    private String telephone;
    @Column(unique = true)
    private String wechat;

    private Integer state;

    @ManyToMany
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = " role_id", referencedColumnName = "id")}
    )
    private List<Role> roles;
}
