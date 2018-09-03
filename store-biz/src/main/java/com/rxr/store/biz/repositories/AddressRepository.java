package com.rxr.store.biz.repositories;

import com.rxr.store.common.entities.Address;
import com.rxr.store.common.entities.Agency;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends BaseRepository<Address, Long> {
    Address findAddressByAgency_IdAndStatus(Long agencyId, Integer status);

    @Modifying
    @Query("update Address set status = 0 where agency = :agency")
    void updateAddress(@Param("agency") Agency agency);
}
