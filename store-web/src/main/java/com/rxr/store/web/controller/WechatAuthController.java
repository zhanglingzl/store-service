package com.rxr.store.web.controller;

import com.rxr.store.biz.service.DigitalWalletService;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.DigitalWallet;
import com.rxr.store.common.util.XmlUtils;
import com.rxr.store.core.JWTToken;
import com.rxr.store.core.util.JWTHelper;
import com.rxr.store.web.common.dto.RestResponse;
import com.rxr.store.wechat.model.WechatAuth;
import com.rxr.store.wechat.model.menu.Menu;
import com.rxr.store.wechat.service.WechatAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 无需登录验证的
 */
@Slf4j
@RestController
@RequestMapping("/wechat/auth")
public class WechatAuthController {
    private final WechatAuthService wechatAuthService;

    private final DigitalWalletService walletService;

    @Autowired
    public WechatAuthController(DigitalWalletService walletService, WechatAuthService wechatAuthService) {
        this.walletService = walletService;
        this.wechatAuthService = wechatAuthService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void auth(WechatAuth auth, HttpServletResponse response) {

        System.out.println("success");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (wechatAuthService.checkSignature(auth)) {
                out.write(auth.getEchostr());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void auth(HttpServletRequest request) {
        try {

            SAXReader reader = new SAXReader();
            Document document = reader.read(request.getInputStream());
            Element root = document.getRootElement();
            Map<String, String> result = new HashMap<>();
            log.info("微信推送事件:"+document.asXML());
            XmlUtils.parseElement(root.elements(), result);
            String wechatId = result.get("FromUserName");
            Agency agency = this.wechatAuthService.findAgencyByWechatId(wechatId);
            if(agency == null) {
                agency = new Agency();
                agency.setWechatId(wechatId);
                switch (result.get("Event")) {
                    case "subscribe":
                        if(StringUtils.isBlank(result.get("EventKey"))) {
                            agency.setParentId(10000L);
                        } else {
                            agency.setParentId(Long.valueOf(result.get("EventKey").split("_")[1]));
                        }
                        agency.setType(0);
                        agency.setLevel(0);
                        agency = this.wechatAuthService.saveAgency(agency);
                        DigitalWallet digitalWallet = new DigitalWallet();
                        digitalWallet.setAgency(agency);
                        this.walletService.saveWallet(digitalWallet);
                        break;
                    case "SCAN":
                        agency.setParentId(Long.valueOf(result.get("EventKey")));
                        agency.setType(0);
                        agency.setLevel(0);
                        agency = this.wechatAuthService.saveAgency(agency);
                        DigitalWallet digitalWallet1 = new DigitalWallet();
                        digitalWallet1.setAgency(agency);
                        this.walletService.saveWallet(digitalWallet1);
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/createMenu", method = RequestMethod.GET)
    public RestResponse<String> createMenu() throws Exception{
        this.wechatAuthService.createMenu(new Menu());
        return RestResponse.success();
    }

    /**
     * 微信获取授权Code,测试使用
     *
     */
    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public void getAuthCode(HttpServletResponse response,
                            //@RequestParam("redirect_uri") String redirectUri,
                            String code)
            throws Exception{
        String redirectUri = "http://wechat.greenleague.xin/callback/wechat-code";
        //System.out.println(code);
        response.sendRedirect(redirectUri+"/?code="+code);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public RestResponse login(@RequestParam("code") String code) {
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()) {
            Agency agency = wechatAuthService.findAgencyByCode(code);
            //Agency agency = wechatAuthService.findAgencyByWechatId("oArUD1huOrlocCNmH4UgheHEBIgc");
            if(agency == null) {
                throw new RuntimeException("未找到agency");
            }
            JWTToken jwtToken = new JWTToken(JWTHelper.createToken(agency.getWechatId()));
            subject.login(jwtToken);
        }
        return RestResponse.success(subject.getPrincipal());
    }

    @GetMapping(value = "/product/qrcode")
    public void productBySerialNo(@RequestParam("serialNo") String serialNo) {
        System.out.println(serialNo);
    }


}
