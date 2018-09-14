package com.rxr.store.biz.repositories;

import com.rxr.store.common.entity.Address;
import com.rxr.store.common.entity.Agency;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends BaseRepository<Address, Long> {
    Address findAddressByAgency_IdAndStatus(Long agencyId, Integer status);
    List<Address> findAddressesByAgency_IdOrderByStatusDesc(Long agencyId);
    @Modifying
    @Query("update Address set status = 0 where agency = :agency")
    void updateAddress(@Param("agency") Agency agency);
    Address findAddressesById(Long id);
    void deleteAddressById(Long id);
    @Modifying
    @Query("update Address set status = 1 where id = :id")
    void updateAddressById(@Param("id") Long id);
}
