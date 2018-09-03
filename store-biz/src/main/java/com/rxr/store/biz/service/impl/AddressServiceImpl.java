package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.AddressRepository;
import com.rxr.store.biz.service.AddressService;
import com.rxr.store.common.entities.Address;
import com.rxr.store.common.entities.Agency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Override
    public Address findAddressByAgency(Agency agency) {
        return addressRepository.findAddressByAgency_IdAndStatus(agency.getId(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address saveAddress(Address address) {
        if(address.getStatus() == 1) {
            this.addressRepository.updateAddress(address.getAgency());
        }
        return this.addressRepository.save(address);
    }
}
