package com.rxr.store.biz.repositories;


import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.DigitalWallet;

public interface DigitalWalletRepository extends BaseRepository<DigitalWallet, Long> {
    DigitalWallet findDigitalWalletByAgency(Agency agency);
}
