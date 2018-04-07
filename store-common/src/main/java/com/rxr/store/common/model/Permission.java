package com.rxr.store.common.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sys_permission")
public class Permission extends BaseEntity {
    private String resourceType;
    private String name;
    private String url;
    /**父编号*/
    private Long parentId;

    /**父编号列表*/
    private String parentIds;

    private Integer available;
    private Integer sort;
    private String iocn;

    @ManyToMany
    @JoinTable(name = "sys_role_permission",
            joinColumns = {@JoinColumn(name = "permission_id" ,referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles;

}
