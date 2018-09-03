package com.rxr.store.wechat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.rxr.store.common.entities.Agency;
import com.rxr.store.common.util.DateHelper;
import com.rxr.store.wechat.model.AccessToken;
import com.rxr.store.wechat.model.Message;
import com.rxr.store.wechat.model.WechatAuth;
import com.rxr.store.wechat.model.menu.Button;
import com.rxr.store.wechat.model.menu.Menu;
import com.rxr.store.wechat.repositories.WechatAuthRepository;
import com.rxr.store.wechat.service.WechatAuthService;
import com.rxr.store.wechat.util.AuthUtil;
import com.rxr.store.wechat.util.JsonUtil;
import com.rxr.store.wechat.util.SHA1;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Log
@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    private static AccessToken token;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private WechatAuthRepository repository;

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
        String temp = SHA1.encode(builder.toString());
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

    private void getQrCode(String ticket,String pageUrl) throws Exception{
        String url = AuthUtil.QR_CODE_URL.replace("TICKET", ticket);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url,HttpMethod.GET, entity, byte[].class);
        byte[] imageBytes = response.getBody();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new ByteArrayInputStream(imageBytes);
            File file = new File(pageUrl);
            if (!file.exists()) {
                file.createNewFile();
            }

            outputStream = new FileOutputStream(file);
            int len;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }

            if(outputStream != null) {
                outputStream.close();
            }
        }

    }
}
