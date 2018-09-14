package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.AddressRepository;
import com.rxr.store.biz.service.AddressService;
import com.rxr.store.common.entity.Address;
import com.rxr.store.common.entity.Agency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<Address> listAddresses(Agency agency) {
        return addressRepository.findAddressesByAgency_IdOrderByStatusDesc(agency.getId());
    }

    @Override
    public Address findAddressById(Long id) {
        return this.addressRepository.findAddressesById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddressById(Long id) {
        this.addressRepository.deleteAddressById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddressStatus(Long id, Agency agency) {
        this.addressRepository.updateAddress(agency);
        this.addressRepository.updateAddressById(id);

    }
}
