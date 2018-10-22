package com.rxr.store.wechat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.rxr.store.biz.service.TradeService;
import com.rxr.store.common.dto.WechatJSPay;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.common.form.TradeForm;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WechatAuthServiceImpl implements WechatAuthService {
    //expireAfterWrite 在指定时间内没有写操作就删除
    //expireAfterAccess 指定时间内没有读写操作
    private final Cache<String, String> weChatCache =  CacheBuilder.newBuilder().maximumSize(100)
            .expireAfterWrite(110, TimeUnit.MINUTES).build();

    private final RestTemplate restTemplate;
    private final WechatAuthRepository repository;
    private final TradeService tradeService;

    @Autowired
    public WechatAuthServiceImpl(RestTemplate restTemplate, WechatAuthRepository repository, TradeService tradeService) {
        this.restTemplate = restTemplate;
        this.repository = repository;
        this.tradeService = tradeService;
    }


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
        btn.setName("润玺尔");
        Button[] subButon1 = new Button[] {
            new Button(Button.Type.view,"公司简介","http://www.runxier.com/introduce/company-profile"),
            new Button(Button.Type.view,"资质认证","http://www.runxier.com/introduce/certification"),
            new Button(Button.Type.view,"培训资料","http://www.runxier.com/introduce/train"),
            new Button(Button.Type.view,"成为VIP会员","http://www.runxier.com/introduce/agency")
        };
        btn.setSubButton(subButon1);
        Button[] buttons = new Button[] {
            new Button(Button.Type.view,"进入商城","http://www.runxier.com/wechat/home"),
            new Button(Button.Type.view,"个人中心","http://www.runxier.com/wechat/personal-center"),
            btn,
        };
        menu.setButton(buttons);
        String menuJson = JsonUtil.obj2json(menu);
        String url = AuthUtil.CREATE_MENU_URL.replace("ACCESS_TOKEN", getAccessToken());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> result = restTemplate.postForEntity(url,menuJson, String.class);
        return JsonUtil.json2pojo(result.getBody(),Message.class);
    }

    @Override
    public Agency findAgencyByWechatId(String wechatId) {
        return repository.findAgencyByWechatId(wechatId);
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
                this.repository.save(agency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agency;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Agency saveAgency(Agency agency) {
        return this.repository.save(agency);
    }

    @Override
    public Agency findAgencyById(Long id) {
        return repository.findAgencyById(id);
    }

    @Override
    public Agency findQrCodeTicket(Long id) {
        try {
            Agency agency = this.findAgencyById(id);
            if(StringUtils.isBlank(agency.getTicket())
                    || agency.getTicketExpire() == null
                    || DateHelper.isBefore(agency.getTicketExpire())) {
                String url = AuthUtil.QR_CODE_TICKET_URL.replace("TOKEN", getAccessToken());
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
                agency = this.repository.save(agency);
            }
            return agency;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public WechatConfig getWechatConfig(String referer) {
        try {
            WechatConfig config = new WechatConfig();
            config.setNonceStr(RandomStringUtils.random(24, true, true));
            config.setTimestamp(System.currentTimeMillis() / 1000);
            config.setAppId(AuthUtil.APP_ID);
            String signStr = "jsapi_ticket="+ getJsSdkTicket() +"&noncestr=" +
                    config.getNonceStr()+"&timestamp="+ config.getTimestamp() +"&url="+referer;
            config.setSignature(HashUtils.getSHA1(signStr));
            log.info("微信页面加载参数: {}, 加密之前的值: {}", config, signStr);
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
            //发送消息
            Agency agency = this.findAgencyByWechatId(result.get("openid"));
            Agency parentAgency = this.findAgencyById(agency.getParentId());
            Trade temp = this.tradeService.findTradeByTradeNo(result.get("out_trade_no"));
            this.sendTradeMessage(agency.getWechatId(),agency, result, temp);
            if(agency.getParentId() != null && agency.getParentId() != 10000) {
                this.sendTradeMessage(parentAgency.getWechatId(), agency, result, temp);
            }

        } else {
            trade.setPayStatus(-1);
        }
        trade.setBankType(result.get("bank_type"));
        trade.setPayEndTime(DateHelper.format(result.get("time_end"),"yyyyMMddHHmmss"));
        trade.setTransactionId(result.get("transaction_id"));
        trade.setTradeNo(result.get("out_trade_no"));
        this.tradeService.updateTrade(trade);
    }

    private void sendTradeMessage(String wechatId, Agency agency, Map<String, String> result, Trade trade) {
        try {
            Date payEnd = DateHelper.format(result.get("time_end"), "yyyyMMddHHmmss");
            String url = AuthUtil.MESSAGE_SEND_URL.replace("ACCESS_TOKEN", getAccessToken());
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("touser", wechatId);
            jsonObject.put("template_id", AuthUtil.MESSAGE_TEMPLATE_ID);
            Map<Object, Object> data = ImmutableMap.builder()
                    .put("first", ImmutableMap.builder()
                            .put("value",MessageFormat.format("恭喜{0} 会员编号{1}购买成功",
                            agency.getName(), String.valueOf(agency.getId())))
                            .put("color", "#173177")
                            .build())
                    .put("keyword1", ImmutableMap.builder()
                            .put("value", trade.getTradeNo())
                            .put("color", "#173177").build())
                    .put("keyword2", ImmutableMap.builder()
                            .put("value", "润玺尔*唇膏")
                            .put("color", "#173177").build())
                    .put("keyword3", ImmutableMap.builder()
                            .put("value", trade.getTotalCount())
                            .put("color", "#173177").build())
                    .put("keyword4", ImmutableMap.builder()
                            .put("value", trade.getPayableAmount())
                            .put("color", "#173177").build())
                    .put("remark", ImmutableMap.builder()
                            .put("value","欢迎再次购买！ 付款时间: "
                                    + DateHelper.format(payEnd,"yyyy-MM-dd"))
                            .put("color", "#173177").build())
                    .build();
            jsonObject.put("data", data);
            HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(),headers);
            String message = restTemplate.postForObject(url, entity, String.class);
            log.info(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        //elementMap.put("total_fee", "10");
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAgencyLevel(Agency agency) {
        double amount = this.tradeService.getTotalAmount(agency);
        if(amount > 300) {
            this.repository.updateAgencyLevelById(1, agency.getId());
        }
    }

    @Override
    public WechatJSPay pendingPay(String tradeNo) {
        Trade trade = this.tradeService.findTradeByTradeNo(tradeNo);
        return this.getWechatJsPay(trade);
    }

    private void findAgencyByWechat(String accessToken, Agency agency) throws Exception {
        String url = AuthUtil.AUTH_USER_INFO_URL.replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", agency.getWechatId());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        JsonNode node = JsonUtil.json2pojo(result.getBody(), JsonNode.class);
        agency.setAvatar(node.get("headimgurl").asText());
        agency.setGender(node.get("sex").asText());
        agency.setLikeName(node.get("nickname").asText());
    }

    @Override
    public void sendShippingMessage(TradeForm tradeForm) {
        try {
            String url = AuthUtil.MESSAGE_SEND_URL.replace("ACCESS_TOKEN", getAccessToken());
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("touser", tradeForm.getAgency().getWechatId());
            jsonObject.put("template_id", AuthUtil.MESSAGE_TEMPLATE_ID);
            Map<Object, Object> data = ImmutableMap.builder()
                    .build();
            jsonObject.put("data", data);
            HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(),headers);
            String message = restTemplate.postForObject(url, entity, String.class);
            log.info(message);
        } catch (Exception e) {
            log.error("发送物流信息错误", e);
        }
    }

    private String getAccessToken() throws Exception {
        String accessToken = weChatCache.getIfPresent("WX_ACCESS_TOKEN_KEY");
        if(StringUtils.isBlank(accessToken)) {
            String url = AuthUtil.ACCESS_TOKEN_URL.replace("APPID", AuthUtil.APP_ID)
                    .replace("APPSECRET", AuthUtil.APP_SECRET);
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
            log.info(result.toString());
            AccessToken token = JsonUtil.json2pojo(result.getBody(),AccessToken.class);
            weChatCache.put("WX_ACCESS_TOKEN_KEY", token.getAccessToken());
            log.info("AccessToken: {}", token);
        }
        return weChatCache.getIfPresent("WX_ACCESS_TOKEN_KEY");
    }

    private String getJsSdkTicket() throws Exception {
        String jsSdkTicket = weChatCache.getIfPresent("WX_JS_SDK_TICKET");
        if(StringUtils.isBlank(jsSdkTicket)) {
            String url=AuthUtil.JS_API_TICKET_URL.replace("ACCESS_TOKEN", getAccessToken());
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
            AccessToken jsSdkToken = JsonUtil.json2pojo(result.getBody(),AccessToken.class);
            log.info("jsSdkToken: {}", jsSdkToken);
            weChatCache.put("WX_JS_SDK_TICKET", jsSdkToken.getTicket());
        }
        return weChatCache.getIfPresent("WX_JS_SDK_TICKET");
    }
}
