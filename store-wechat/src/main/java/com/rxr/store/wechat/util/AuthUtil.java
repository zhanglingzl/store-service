package com.rxr.store.wechat.util;

import com.rxr.store.wechat.service.impl.WechatAuthServiceImpl;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class AuthUtil {
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" +
            "client_credential&appid=APPID&secret=APPSECRET";
    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/" +
            "create?access_token=ACCESS_TOKEN";
    public static final String AUTH_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID" +
            "&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE#wechat_redirect";
    public static final String AUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID" +
            "&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static final String AUTH_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&" +
            "openid=OPENID&lang=zh_CN";
    public static final String QR_CODE_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
    public static final String JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    public static final String TOKEN = "storeService";
    public static final String APP_ID = "wxfd8a4a8b67963cf9";
    public static final String APP_SECRET = "242f680feb79c51b39508be13db6f2f0";


    public static final String MCH_ID = "1505418441";
    public static final String PAY_NOTIFY_URL = "http://service.runxier.com/wechat/auth/notify_url";
    public static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String PAY_KEY = "gv3RQQMwly5QjpQ646pZrOwF6eMMoyxA";

    public static final String MESSAGE_TEMPLATE_ID = "2vARsqoMvQ_ZqFbF9RbUVksZgezcCLvTTopZ9NXT7yc";
    public static final String MESSAGE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

}
