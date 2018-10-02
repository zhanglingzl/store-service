package com.rxr.store.biz.service;

import com.rxr.store.common.dto.WalletDTO;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.DigitalWallet;
import com.rxr.store.common.entity.Withdraw;
import com.rxr.store.common.form.WalletForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DigitalWalletService {
    WalletDTO getWalletInfo(WalletForm walletForm);

    void saveWallet(DigitalWallet digitalWallet);

    Double[] getSemiannualAmount(WalletForm walletForm);

    DigitalWallet getWallet(Agency agency);

    Page<Withdraw> listWithdraws(Long agencyId, Pageable pageable);
}
