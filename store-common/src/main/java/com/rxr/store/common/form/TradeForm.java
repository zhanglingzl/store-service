package com.rxr.store.common.form;

import com.rxr.store.common.entity.Agency;
import lombok.Data;

import java.util.List;

@Data
public class TradeForm {
    private Integer payStatus;
    private Integer agencyFlag;
    private Agency agency;
    private List<Long> agencyIds;
    private String name;
}
