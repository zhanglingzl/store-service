package com.rxr.store.web.controller;

import com.rxr.store.biz.service.AddressService;
import com.rxr.store.common.entity.Address;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.web.common.dto.RestResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

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

    @GetMapping("/address/list")
    public RestResponse<List<Address>> listAddresses() {
        Subject subject = SecurityUtils.getSubject();
        Agency agency = (Agency) subject.getPrincipal();
        List<Address> address = this.addressService.listAddresses(agency);
        return RestResponse.success(address);
    }

    @GetMapping("/address/{id}")
    public RestResponse<Address> getAddressById(@PathVariable("id") Long id) {
        return RestResponse.success(this.addressService.findAddressById(id));
    }

    @DeleteMapping("/address/{id}")
    public RestResponse deleteAddressById(@PathVariable("id") Long id) {
        this.addressService.deleteAddressById(id);
        return RestResponse.success();
    }

    @PutMapping("/address/change-status/{id}")
    public RestResponse changeAddressStatus(@PathVariable("id") Long id){
        Subject subject = SecurityUtils.getSubject();
        Agency agency = (Agency) subject.getPrincipal();
        this.addressService.updateAddressStatus(id, agency);
        return RestResponse.success();
    }
}
