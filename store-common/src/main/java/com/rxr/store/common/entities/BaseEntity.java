package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
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
    @Column(name = "create_time",columnDefinition="timestamp default current_timestamp")
    private Timestamp createTime;
    @Column(name = "update_operator")
    private String updateOperator;
    @Column(name = "update_time" ,columnDefinition="timestamp default current_timestamp on update current_timestamp")
    private Timestamp updateTime;

    private String remark;
}
