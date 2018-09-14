package com.rxr.store.biz.repositories;

import com.rxr.store.common.entity.Trade;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface TradeRepository extends BaseRepository<Trade, Long> {
    @Modifying
    @Query("update Trade set payStatus= :payStatus, transactionId = :transactionId, bankType = :bankType, " +
            "payEndTime = :payEndTime where tradeNo = :tradeNo")
    void updateTrade(@Param("payStatus") Integer payStatus, @Param("transactionId") String transactionId,
                     @Param("bankType") String bankType, @Param("payEndTime") Date payEndTime,
                     @Param("tradeNo") String tradeNo);

}
