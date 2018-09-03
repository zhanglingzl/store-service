package com.rxr.store.common.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@Table(name = "sys_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    @Column(name = "role_name")
    private String role;
    @Column(name = "role_desc")
    private String desc;
    /**是否可用*/
    @Column(name = "role_available")
    private Integer available;

    @ManyToMany
    @JoinTable(name = "sys_role_permission",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "per_id")}
    )
    private List<Permission> permissions;

    @ManyToMany
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> users;
}
