package com.rxr.store.common.form;

import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import lombok.Data;

import java.util.List;

@Data
public class TradeForm {
    private Integer payStatus;
    private Integer agencyFlag;
    private Agency agency;
    private List<Long> agencyIds;
    private String name;
    private Trade trade;
    private String agencyName;
    private String agencyId;
    private String tradeNo;
    private String[] betweenDate;
    private String shipStatus;
    private String telephone;
    private String address;
    private String trackingNo;
}
