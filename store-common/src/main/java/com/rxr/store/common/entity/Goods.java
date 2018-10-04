package com.rxr.store.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@Entity
@Table(name = "rxr_goods")
@EqualsAndHashCode(callSuper = true)
public class Goods extends BaseEntity{
    @Column(name = "trade_no")
    private String tradeNo;
    @Column(unique = true)
    private String serialNo;
    private String productNo;
    private String productName;
    @Column(name="product_amount")
    private Double amount;
    /**会员价*/
    @Column(name="product_vip_amount")
    private Double vipAmount;


}