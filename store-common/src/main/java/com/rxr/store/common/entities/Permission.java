package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "sys_permission")
public class Permission extends BaseEntity {
    @Column(name = "per_resource_type")
    private String resourceType;
    @Column(name = "per_name")
    private String name;
    @Column(name="per_url")
    private String url;
    /**父编号*/
    @Column(name = "per_pid")
    private Long parentId;

    /**父编号列表*/
    @Column(name = "per_pids")
    private String parentIds;
    @Column(name = "per_available")
    private Integer available;
    @Column(name = "per_sort")
    private Integer sort;
    @Column(name = "per_iocn")
    private String iocn;

    @ManyToMany
    @JoinTable(name = "sys_role_permission",
            joinColumns = {@JoinColumn(name = "per_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> roles;

}
