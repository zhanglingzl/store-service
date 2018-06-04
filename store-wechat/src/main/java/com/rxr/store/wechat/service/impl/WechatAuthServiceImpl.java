package com.rxr.store.wechat.service.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.rxr.store.common.entities.Agency;
import com.rxr.store.common.util.DateHelper;
import com.rxr.store.wechat.model.AccessToken;
import com.rxr.store.wechat.model.Message;
import com.rxr.store.wechat.model.WechatAuth;
import com.rxr.store.wechat.model.WechatInfo;
import com.rxr.store.wechat.model.menu.Button;
import com.rxr.store.wechat.model.menu.Menu;
import com.rxr.store.wechat.repositories.WechatAuthRepository;
import com.rxr.store.wechat.service.WechatAuthService;
import com.rxr.store.wechat.util.AuthUtil;
import com.rxr.store.wechat.util.JsonUtil;
import com.rxr.store.wechat.util.SHA1;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

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
        StringBuffer buffer = new StringBuffer();
        for(int i =0 ;i<str.length;i++){
            buffer.append(str[i]);
        }
        //进行sha1加密
        String temp = SHA1.encode(buffer.toString());
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
            new Button(Button.Type.view,"公司简介","http://store.vicp.la:8888/wechat/companyProfile"),
            new Button(Button.Type.view,"培训资料","http://store.vicp.la:8888/wechat/train"),
            new Button(Button.Type.view,"成为代理","http://store.vicp.la:8888/wechat/agency"),
            new Button(Button.Type.view,"个人中心","http://store.vicp.la:8888/wechat/personal")

        };
        btn.setSubButton(subButon);
        Button[] buttons = new Button[] {
            new Button(Button.Type.view,"进入商城","http://store.vicp.la:8888/wechat/home"),
            new Button(Button.Type.view, "我的二维码", "http://store.vicp.la:8888/wechat/qrcode")
        };
        menu.setButton(buttons);
        String menuJson = JsonUtil.obj2json(menu);
        initAccessToken();
        String url = AuthUtil.CREATE_MENU_URL.replace("ACCESS_TOKEN", token.getAccessToken());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> result = restTemplate.postForEntity(url,menuJson, String.class);
        return JsonUtil.json2pojo(result.getBody(),Message.class);
    }


    private void initAccessToken() throws Exception {
        String url = AuthUtil.ACCESS_TOKEN_URL.replace("APPID", AuthUtil.APP_ID)
                .replace("APPSECRET", AuthUtil.APP_SECRET);

        if(token == null || DateHelper.isBefore(token.getExpiresDate())) {
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
            token = JsonUtil.json2pojo(result.getBody(),AccessToken.class);
            token.setExpiresDate(DateHelper.plusMinutes(token.getExpiresIn()));
        }

    }
}
