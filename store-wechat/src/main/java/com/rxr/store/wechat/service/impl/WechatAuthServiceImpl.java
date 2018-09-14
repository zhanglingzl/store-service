package com.rxr.store.wechat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.rxr.store.biz.service.TradeService;
import com.rxr.store.common.dto.WechatJSPay;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.common.util.DateHelper;
import com.rxr.store.common.util.StringHelper;
import com.rxr.store.common.util.XmlUtils;
import com.rxr.store.wechat.model.AccessToken;
import com.rxr.store.wechat.model.Message;
import com.rxr.store.wechat.model.WechatAuth;
import com.rxr.store.wechat.model.WechatConfig;
import com.rxr.store.wechat.model.menu.Button;
import com.rxr.store.wechat.model.menu.Menu;
import com.rxr.store.wechat.repositories.WechatAuthRepository;
import com.rxr.store.wechat.service.WechatAuthService;
import com.rxr.store.wechat.util.AuthUtil;
import com.rxr.store.wechat.util.HashUtils;
import com.rxr.store.wechat.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    private static AccessToken token;
    private static AccessToken jsSdkTicket;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatAuthRepository repository;

    @Autowired
    private TradeService tradeService;

    @Override
    public boolean checkSignature(WechatAuth auth){
        String[] str = new String[]{AuthUtil.TOKEN,auth.getTimestamp(), auth.getNonce()};
        //排序
        Arrays.sort(str);
        //拼接字符串
        StringBuilder builder = new StringBuilder();
        for(int i =0 ;i<str.length;i++){
            builder.append(str[i]);
        }
        //进行sha1加密
        String temp = HashUtils.getSHA1(builder.toString());
        //与微信提供的signature进行匹对
        return auth.getSignature().equals(temp);
    }

    @Override
    public Message createMenu(Menu menu) throws Exception {
        menu = new Menu();
        Button btn = new Button();
        btn.setName("服务中心");
        Button[] subButon = new Button[] {
            new Button(Button.Type.view,"资质认证","http://wechat.greenleague.xin/wechat/certification"),
            new Button(Button.Type.view,"公司简介","http://wechat.greenleague.xin/wechat/company-profile"),
            new Button(Button.Type.view,"培训资料","http://wechat.greenleague.xin/wechat/train"),
            new Button(Button.Type.view,"成为代理","http://wechat.greenleague.xin/wechat/agency"),
            new Button(Button.Type.view,"个人中心","http://wechat.greenleague.xin/wechat/personal-center")

        };
        btn.setSubButton(subButon);
        Button[] buttons = new Button[] {
            new Button(Button.Type.view,"进入商城","http://wechat.greenleague.xin/wechat/home"),
            new Button(Button.Type.view, "我的二维码", "http://wechat.greenleague.xin/wechat/qr-code"),
                btn
        };
        menu.setButton(buttons);
        String menuJson = JsonUtil.obj2json(menu);
        initAccessToken();
        String url = AuthUtil.CREATE_MENU_URL.replace("ACCESS_TOKEN", token.getAccessToken());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> result = restTemplate.postForEntity(url,menuJson, String.class);
        return JsonUtil.json2pojo(result.getBody(),Message.class);
    }

    @Override
    public Agency findAgencyByWechatId(String wechatId) {
        return repository.findAgencyByWechatId(wechatId);
    }

    private void initAccessToken() throws Exception {
        String url = AuthUtil.ACCESS_TOKEN_URL.replace("APPID", AuthUtil.APP_ID)
                .replace("APPSECRET", AuthUtil.APP_SECRET);

        if(token == null || DateHelper.isBefore(token.getExpiresDate())) {
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
            token = JsonUtil.json2pojo(result.getBody(),AccessToken.class);
            token.setExpiresDate(DateHelper.plusSecond(token.getExpiresIn()));
        }

    }

    @Override
    public Agency findAgencyByCode(String code) {
        String url = AuthUtil.AUTH_ACCESS_TOKEN_URL.replace("APPID", AuthUtil.APP_ID)
                .replace("SECRET", AuthUtil.APP_SECRET).replace("CODE",code);
        String result = restTemplate.getForObject(url, String.class);
        Agency agency = null;
        try {
            JsonNode node = JsonUtil.json2pojo(result, JsonNode.class);
            String accessToken = node.get("access_token").asText();
            String openId = node.get("openid").asText();
            agency = this.findAgencyByWechatId(openId);
            if(agency != null && StringUtils.isBlank(agency.getAvatar())) {
                this.findAgencyByWechat(accessToken,agency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agency;
    }

    @Override
    public void saveAgency(Agency agency) {
        this.repository.save(agency);
    }

    @Override
    public Agency findAgencyById(Long id) {
        return repository.findAgencyById(id);
    }

    @Override
    public String findQrCodeTicket(Long id) {
        try {
            Agency agency = this.findAgencyById(id);
            if(StringUtils.isBlank(agency.getTicket())
                    || agency.getTicketExpire() == null
                    || DateHelper.isBefore(agency.getTicketExpire())) {
                initAccessToken();
                String url = AuthUtil.QR_CODE_TICKET_URL.replace("TOKEN", token.getAccessToken());
                HttpHeaders headers = new HttpHeaders();
                MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
                headers.setContentType(type);
                String requestJson = "{\"expire_seconds\": 2592000, \"action_name\":" +
                        " \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " +
                        id +
                        "}}}";
                HttpEntity<String> entity = new HttpEntity<>(requestJson,headers);
                String result = restTemplate.postForObject(url, entity, String.class);
                JsonNode node = JsonUtil.json2pojo(result, JsonNode.class);
                String ticket = node.get("ticket").asText();
                agency.setTicket(ticket);
                agency.setTicketExpire(DateHelper.plusSecond(node.get("expire_seconds").asInt()));
                this.repository.save(agency);
            }
            return agency.getTicket();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public WechatConfig getWechatConfig(String referer) {
        try {
            WechatConfig config = new WechatConfig();
            if(jsSdkTicket == null || DateHelper.isBefore(jsSdkTicket.getExpiresDate())) {
                initAccessToken();
                String url=AuthUtil.JS_API_TICKET_URL.replace("ACCESS_TOKEN", token.getAccessToken());
                ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
                jsSdkTicket = JsonUtil.json2pojo(result.getBody(),AccessToken.class);
                jsSdkTicket.setExpiresDate(DateHelper.plusSecond(token.getExpiresIn()));
            }
            config.setNonceStr(RandomStringUtils.random(24, true, true));
            config.setTimestamp(System.currentTimeMillis());
            config.setAppId(AuthUtil.APP_ID);
            String signStr = "jsapi_ticket="+ jsSdkTicket.getTicket() +"&noncestr=" +
                    config.getNonceStr()+"&timeStamp="+ config.getTimestamp() +"&url="+referer;

            config.setSignature(HashUtils.getSHA1(signStr));
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public WechatJSPay wechatJSPay(Trade trade) {
        String tradeNo = StringHelper.getPayTradeNo();
        trade.setTradeNo(tradeNo);
        trade.getGoodsList().forEach(goods -> {
            goods.setTradeNo(tradeNo);
            goods.setId(null);
        });
        trade.setPayStatus(0);
        trade.setShipStatus(0);
        return this.getWechatJsPay(trade);
    }

    @Override
    public void savePayByWechatNotify(Document document) {
        Map<String, String> result = new HashMap<>();
        XmlUtils.parseElement(document.getRootElement().elements(), result);
        Trade trade = new Trade();
        if("SUCCESS".equals(result.get("result_code")) && "SUCCESS".equals(result.get("return_code"))) {
            trade.setPayStatus(1);
        } else {
            trade.setPayStatus(-1);
        }
        trade.setBankType(result.get("bank_type"));
        trade.setPayEndTime(DateHelper.format(result.get("time_end"),"yyyyMMddHHmmss"));
        trade.setTransactionId(result.get("transaction_id"));
        trade.setTradeNo(result.get("out_trade_no"));
        this.tradeService.updateTrade(trade);
    }

    private WechatJSPay getWechatJsPay(Trade trade) {
        WechatJSPay wechatJSPay = new WechatJSPay();
        wechatJSPay.setAppId(AuthUtil.APP_ID);
        wechatJSPay.setSignType("MD5");
        wechatJSPay.setNonceStr(RandomStringUtils.random(24, true, true));
        wechatJSPay.setTimestamp(System.currentTimeMillis());
        getPrepayId(trade);
        if(trade.getPayStatus() == 0) {
            wechatJSPay.setPrepayId(trade.getPrepayId());
            Map<String, String> singMap = new HashMap<>(16);
            singMap.put("appId", wechatJSPay.getAppId());
            singMap.put("nonceStr", wechatJSPay.getNonceStr());
            singMap.put("package", wechatJSPay.getPrepayId());
            singMap.put("signType", wechatJSPay.getSignType());
            singMap.put("timeStamp", String.valueOf(wechatJSPay.getTimestamp()));
            wechatJSPay.setPaySign(HashUtils.getSign(singMap, AuthUtil.PAY_KEY, HashUtils.SignType.MD5));
        } else {
            wechatJSPay = null;
        }
        this.tradeService.save(trade);
        return wechatJSPay;
    }

    private void getPrepayId(Trade trade) {
        Map<String, String> elementMap = new HashMap<>();
        elementMap.put("appid", AuthUtil.APP_ID);
        elementMap.put("mch_id", AuthUtil.MCH_ID);
        elementMap.put("nonce_str", RandomStringUtils.random(24, true, true));
        elementMap.put("body", "润玺尔-唇膏");
        elementMap.put("device_info", "WEB");
        elementMap.put("out_trade_no", trade.getTradeNo());
        elementMap.put("total_fee", String.valueOf(trade.getPayableAmount()
                .multiply(new BigDecimal("100")).intValue()));
        elementMap.put("total_fee", "10");
        elementMap.put("spbill_create_ip", trade.getCreateIp());
        elementMap.put("sign_type","MD5");
        elementMap.put("notify_url", AuthUtil.PAY_NOTIFY_URL);
        elementMap.put("trade_type", "JSAPI");
        elementMap.put("openid", trade.getAgency().getWechatId());
        elementMap.put("sign", HashUtils.getSign(elementMap, AuthUtil.PAY_KEY, HashUtils.SignType.MD5));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/xml; charset=UTF-8"));
        String payXml = XmlUtils.getXml(elementMap);
        System.out.println(payXml);
        HttpEntity<String> entity = new HttpEntity<>(payXml,headers);

        String result = restTemplate.postForObject(AuthUtil.PAY_URL, entity, String.class);
        log.info(result);
        try {
            if(result != null) {
                Document document = DocumentHelper.parseText(result);
                Element rootElement = document.getRootElement();
                if("SUCCESS".equals(rootElement.element("return_code").getText()) &&
                        "SUCCESS".equals(rootElement.element("result_code").getText())) {
                    trade.setPayStatus(0);
                    trade.setPrepayId("prepay_id="+ rootElement.element("prepay_id").getText());
                    trade.setPrepayExpireTime(DateHelper.plusMinutes(60 * 2));
                } else {
                    trade.setPayStatus(-1);
                }
            }
        } catch (DocumentException e) {
            trade.setPayStatus(-1);
            log.error(e.getMessage());
        }
    }


    private void findAgencyByWechat(String accessToken, Agency agency) throws Exception {
        String url = AuthUtil.AUTH_USER_INFO_URL.replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", agency.getWechatId());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        JsonNode node = JsonUtil.json2pojo(result.getBody(), JsonNode.class);
        agency.setAvatar(node.get("headimgurl").asText());
        agency.setGender(node.get("sex").asText());
        agency.setName(node.get("nickname").asText());
    }


}
