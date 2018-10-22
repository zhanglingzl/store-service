package com.rxr.store.biz.repositories;

import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TradeRepository extends BaseRepository<Trade, Long> {
    @Modifying
    @Query("update Trade set payStatus= :payStatus, transactionId = :transactionId, bankType = :bankType, " +
            "payEndTime = :payEndTime where tradeNo = :tradeNo")
    void updateTrade(@Param("payStatus") Integer payStatus, @Param("transactionId") String transactionId,
                     @Param("bankType") String bankType, @Param("payEndTime") Date payEndTime,
                     @Param("tradeNo") String tradeNo);

    List<Trade> findAllByPayStatusAndAgencyInAndCreateTimeBetween(Integer payStatus,
                                                                  List<Agency> agencies, Date before, Date end);

    Trade findTradeByTradeNo(String tradeNo);

    List<Trade> findAllByPayStatusAndCreateTimeBefore(Integer payStatus, Date before);

    @Query("select coalesce(sum(payableAmount), 0) from Trade where agency=:agency and payStatus=:payStates")
    Double findTradeByAgencyAndPayStatus(@Param("agency") Agency agency, @Param("payStates") Integer payStatus);

    @Modifying
    @Query("update Trade set trackingName= :trackingName, trackingNo= :trackingNo, shipStatus = :shipStatus where " +
            "tradeNo= :tradeNo")
    void updateShipping(@Param("trackingName") String trackingName, @Param("trackingNo") String trackingNo,
                        @Param("shipStatus") Integer shipStatus, @Param("tradeNo") String tradeNo);

    @Query("select coalesce(sum(payableAmount), 0) as amount, count(id) as totalCount from Trade where payStatus= :payStatus")
    Map<String, Object> findTradeByPayStatus(@Param("payStatus") Integer payStatus);

    List<Trade> findAllByPayStatusAndCreateTimeBetween(Integer payStatus, Date before, Date end);
}
