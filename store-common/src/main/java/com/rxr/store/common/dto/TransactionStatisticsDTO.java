package com.rxr.store.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionStatisticsDTO {
    //交易成功总笔数
    private Long totalCount;
    //成功总金额
    private BigDecimal totalAmount;

    private Long avgCount;
    private BigDecimal avgAmount;
    //每天的交易量
    List<TransactionStatisticsDTO> dayList;
}
