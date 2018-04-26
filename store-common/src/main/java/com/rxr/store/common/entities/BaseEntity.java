package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "create_operator")
    private String createOperator;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_operator")
    private String updateOperator;
    @Column(name = "update_time")
    private Date updateTime;

    private String remark;
}
