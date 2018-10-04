package com.rxr.store.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "rxr_trade")
@EqualsAndHashCode(callSuper = true)
public class Trade extends BaseEntity {
    /**订单号*/
    @Column(unique = true)
    private String tradeNo;
    private String name;
    private String phone;
    private String province;
    private String city;
    /**区*/
    private String country;
    /**详细地址*/
    private String address;
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "trade_id")
    private List<Goods> goodsList;

    private BigDecimal totalAmount;
    private Integer totalCount;
    /**折扣价*/
    private BigDecimal discountAmount;
    /**应付金额 实际付款金额*/
    private BigDecimal payableAmount;
    private String remark;
    /**付款状态, 0: 待支付, 1: 支付成功(待收货), 2: 已完成, -1: 支付失败*/
    private Integer payStatus;
    /**是否已入账 0:未入账, 1: 已入账*/
    private Integer passEntry;

    /**发货状态 0: 未发货 1:已发货*/
    private Integer shipStatus;
    /**微信下单返回的预支付信息, 2小时过期*/
    private String prepayId;
    /**支付过期时间*/
    private Date prepayExpireTime;
    /**支付IP*/
    private String createIp;
    /**-------微信返回信息-------*/
    private String bankType;
    private String transactionId;
    /**支付完成时间*/
    private Date payEndTime;

    /**物流单号*/
    private String trackingNo;
    /**物流公司*/
    private String trackingName;
}
