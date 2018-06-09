package com.rxr.store.wechat.util;

import org.springframework.stereotype.Service;

@Service
public class AuthUtil {
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" +
            "client_credential&appid=APPID&secret=APPSECRET";
    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/" +
            "create?access_token=ACCESS_TOKEN";
    public static final String AUTH_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID" +
            "&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE#wechat_redirect";

    public static final String TOKEN = "storeService";
    public static final String APP_ID = "wxfd8a4a8b67963cf9";
    public static final String APP_SECRET = "242f680feb79c51b39508be13db6f2f0";
}
