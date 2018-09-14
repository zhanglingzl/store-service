package com.rxr.store.biz.service;

import com.rxr.store.common.entity.Address;
import com.rxr.store.common.entity.Agency;

import java.util.List;

public interface AddressService {

    Address findAddressByAgency(Agency agency);

    Address saveAddress(Address address);

    List<Address> listAddresses(Agency agency);

    Address findAddressById(Long id);

    void deleteAddressById(Long id);

    void updateAddressStatus(Long id, Agency agency);
}
