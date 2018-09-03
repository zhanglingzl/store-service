package com.rxr.store.biz.service;

import com.rxr.store.common.entities.Address;
import com.rxr.store.common.entities.Agency;

public interface AddressService {

    Address findAddressByAgency(Agency agency);

    Address saveAddress(Address address);
}
