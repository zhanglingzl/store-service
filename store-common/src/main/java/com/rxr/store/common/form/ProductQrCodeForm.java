package com.rxr.store.common.form;

import com.rxr.store.common.util.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class ProductQrCodeForm {
    /***/
    private String productNo;
    /**生产时间年+周, 如 1820 18年20周*/
    private String dateCode;
    /**产品类目*/
    private String categoryNo;
    /**生产批次*/
    private String batchNo;
    /**序列号*/
    private String serialNo;
    /**箱数*/
    private int productBox;

    public String getSerialNumber(String number) {
        if(StringUtils.isBlank(this.dateCode)) {
            this.dateCode = DateHelper.serialDateCode();
        }
        return this.dateCode +"-" + this.categoryNo + "-" + this.batchNo + "-" + number;
    }
}
