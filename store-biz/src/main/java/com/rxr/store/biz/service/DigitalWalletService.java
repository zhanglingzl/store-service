package com.rxr.store.biz.service;

import com.rxr.store.common.dto.WalletDTO;
import com.rxr.store.common.entity.DigitalWallet;
import com.rxr.store.common.form.WalletForm;

public interface DigitalWalletService {
    WalletDTO getWalletInfo(WalletForm walletForm);

    void saveWallet(DigitalWallet digitalWallet);

    Double[] getSemiannualAmount(WalletForm walletForm);
}
