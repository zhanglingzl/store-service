package com.rxr.store.web.controller;

import com.rxr.store.common.entities.Agency;
import com.rxr.store.web.common.dto.RestResponse;
import com.rxr.store.wechat.service.WechatAuthService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/wechat")
public class WechatController {
    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/parent-agency", method = RequestMethod.GET)
    public RestResponse parentAgency(@RequestParam("parentId") Long parentId) {
        Agency agency = wechatAuthService.findParentAgencyById(parentId);
        return RestResponse.success(agency);
    }

    @RequestMapping(value = "/qr-code",method = RequestMethod.GET)
    public RestResponse qrCode(@RequestParam("id") Long id) {
        String ticket = wechatAuthService.findQrCodeTicket(id);
        return RestResponse.success(ticket);
    }


}
