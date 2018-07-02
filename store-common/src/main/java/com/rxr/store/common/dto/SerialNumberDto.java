package com.rxr.store.common.dto;

import com.rxr.store.common.util.DateHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SerialNumberDto {
    /**生产时间年+周, 如 1820 18年20周*/
    private String dateCode;
    /**产品类目*/
    private String categoryNo;
    /**生产批次*/
    private String batchNo;
    /**序列号*/
    private String serialNo;

    public String getSerialNumber() {
        return DateHelper.serialDateCode()+"-"
                + this.categoryNo + "-"
                + this.batchNo + "-"
                + this.serialNo;
    }
}
