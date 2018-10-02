package com.rxr.store.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "rxr_withdraw")
@EqualsAndHashCode(callSuper = true)
public class Withdraw extends BaseEntity{
    private BigDecimal amount;
    private Date processTime;
    /** 处理状态
     * 0: 待处理
     * 1: 失败
     * 2: 成功
     */
    private Integer status;
    private String remark;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
}
