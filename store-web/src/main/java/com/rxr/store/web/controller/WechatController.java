package com.rxr.store.web.controller;

import com.rxr.store.common.dto.WechatJSPay;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.common.util.StringHelper;
import com.rxr.store.common.util.XmlUtils;
import com.rxr.store.web.common.dto.RestResponse;
import com.rxr.store.wechat.model.WechatConfig;
import com.rxr.store.wechat.service.WechatAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatController {
    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/parent-agency", method = RequestMethod.GET)
    public RestResponse parentAgency(@RequestParam("parentId") Long parentId) {
        Agency agency = wechatAuthService.findAgencyById(parentId);
        return RestResponse.success(agency);
    }


    @PostMapping("/pay")
    public RestResponse<WechatJSPay> pay(@RequestBody Trade trade, HttpServletRequest request) {
        Agency agency = (Agency) SecurityUtils.getSubject().getPrincipal();
        trade.setAgency(agency);
        trade.setCreateIp(StringHelper.getIp(request));
        trade.setPayStatus(0);
        WechatJSPay wechatJSPay = this.wechatAuthService.wechatJSPay(trade);
        log.info(wechatJSPay.toString());
        return RestResponse.success(wechatJSPay);
    }

    @GetMapping("/pendingPay")
    public RestResponse<WechatJSPay> pendingPay(@RequestParam("tradeNo") String tradeNo) {
        WechatJSPay wechatJSPay = this.wechatAuthService.pendingPay(tradeNo);
        log.info(wechatJSPay.toString());
        return RestResponse.success(wechatJSPay);
    }
}
