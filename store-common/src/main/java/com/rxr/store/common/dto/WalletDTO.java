package com.rxr.store.common.dto;

import com.rxr.store.common.entity.DigitalWallet;
import lombok.Data;

@Data
public class WalletDTO {
    private DigitalWallet digitalWallet;
    private Double[] tradeAmount;
    private Double[] firstTradeAmount;
    private Double[] secondTradeAmount;
    private Double[] thirdTradeAmount;
}
