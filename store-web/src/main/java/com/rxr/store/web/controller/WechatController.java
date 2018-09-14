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

    @RequestMapping(value = "/qr-code",method = RequestMethod.GET)
    public RestResponse qrCode(@RequestParam("id") Long id) {
        String ticket = wechatAuthService.findQrCodeTicket(id);
        return RestResponse.success(ticket);
    }

    @GetMapping("/config")
    public RestResponse<WechatConfig> getWechatConfig(HttpServletRequest request) {
        log.info(request.getHeader("Referer"));
        WechatConfig wechatConfig = wechatAuthService.getWechatConfig(request.getHeader("Referer"));
        return RestResponse.success(wechatConfig);
    }

    @PostMapping("/pay")
    public RestResponse<WechatJSPay> pay(@RequestBody Trade trade, HttpServletRequest request) {
        Agency agency = (Agency) SecurityUtils.getSubject().getPrincipal();
        trade.setAgency(agency);
        trade.setCreateIp(StringHelper.getIp(request));
        WechatJSPay wechatJSPay = this.wechatAuthService.wechatJSPay(trade);
        log.info(wechatJSPay.toString());
        return RestResponse.success(wechatJSPay);
    }

    @PostMapping("notify_url")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(request.getInputStream());
            log.info(document.asXML());
            this.wechatAuthService.savePayByWechatNotify(document);
            Map<String, String> map = new HashMap<>();
            map.put("return_code","SUCCESS");
            map.put("return_msg", "OK");
            String xml = XmlUtils.getXml(map);
            log.info(xml);
            response.getWriter().write(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
