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
            new Button(Button.Type.view,"资质认证","http://store.vicp.la:8888/wechat/certification"),
            new Button(Button.Type.view,"公司简介","http://store.vicp.la:8888/wechat/company-profile"),
            new Button(Button.Type.view,"培训资料","http://store.vicp.la:8888/wechat/train"),
            new Button(Button.Type.view,"成为代理","http://store.vicp.la:8888/wechat/agency"),
            new Button(Button.Type.view,"个人中心","http://store.vicp.la:8888/wechat/personal-center")

        };
        btn.setSubButton(subButon);
        Button[] buttons = new Button[] {
            new Button(Button.Type.view,"进入商城","http://store.vicp.la:8888/wechat/home"),
            new Button(Button.Type.view, "我的二维码", "http://store.vicp.la:8888/wechat/qr-code"),
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
            if(agency == null) {
                agency = this.findAgencyByWechat(accessToken,openId);
                agency = this.repository.save(agency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agency;
    }

    @Override
    public Agency findParentAgencyById(Long parentId) {
        return repository.findAgencyById(parentId);
    }

    @Override
    public String findQrCodeTicket(Long id) {
        try {
            initAccessToken();
            String url = AuthUtil.QR_CODE_TICKET_URL.replace("TOKEN", token.getAccessToken());
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            String requestJson = "{\"expire_seconds\": 604800, \"action_name\":" +
                    " \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " +
                    id +
                    "}}}";
            HttpEntity<String> entity = new HttpEntity<>(requestJson,headers);
            String result = restTemplate.postForObject(url, entity, String.class);
            JsonNode node = JsonUtil.json2pojo(result, JsonNode.class);
            String ticket = node.get("ticket").asText();
            String pageUrl = "/var/www/images/agency/"+id+".jpg";
            this.getQrCode(ticket,pageUrl);
            return "images/"+id+".jpg";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Agency findAgencyByWechat(String accessToken, String openId) throws Exception {
        String url = AuthUtil.AUTH_USER_INFO_URL.replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", openId);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        Agency agency = null;
        JsonNode node = JsonUtil.json2pojo(result.getBody(), JsonNode.class);
        agency =  new Agency();
        agency.setWechatId(openId);
        agency.setAvatar(node.get("headimgurl").asText());
        agency.setGender(node.get("sex").asText());
        agency.setName(node.get("nickname").asText());
        return agency;
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
