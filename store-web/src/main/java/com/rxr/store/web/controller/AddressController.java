package com.rxr.store.web.controller;

import com.rxr.store.biz.service.AddressService;
import com.rxr.store.common.entities.Address;
import com.rxr.store.common.entities.Agency;
import com.rxr.store.web.common.dto.RestResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/address")
    public RestResponse<Address> findAgencAddress() {
        Subject subject = SecurityUtils.getSubject();
        Agency agency = (Agency) subject.getPrincipal();
        Address address = this.addressService.findAddressByAgency(agency);
        return RestResponse.success(address);
    }

    @PostMapping("/address")
    public RestResponse<Address> saveAddress(@RequestBody Address address) {
        Subject subject = SecurityUtils.getSubject();
        Agency agency = (Agency) subject.getPrincipal();
        address.setAgency(agency);
        Address newAddress = this.addressService.saveAddress(address);
        return RestResponse.success(newAddress);
    }
}
