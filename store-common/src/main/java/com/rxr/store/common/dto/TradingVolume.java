package com.rxr.store.common.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author ZL
 * @date 2018-10-22 15:14
 **/
@Data
public class TradingVolume {

    /**
     * 交易量
     */
    private int totalCount;

    /**
     * 交易总金额
     */
    private String totalAmount;

    /**
     * 日期
     */
    private String date;

}
