package com.rxr.store.common.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity {
    private String role;
    private String desc;
    /**是否可用*/
    private Integer available;

    @ManyToMany
    @JoinTable(name = "sys_role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")}
    )
    private List<Permission> permissions;

    @ManyToMany
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private List<User> users;
}
