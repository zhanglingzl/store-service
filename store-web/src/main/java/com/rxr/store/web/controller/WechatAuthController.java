package com.rxr.store.web.controller;

import com.rxr.store.common.entities.Agency;
import com.rxr.store.core.JWTToken;
import com.rxr.store.core.util.JWTHelper;
import com.rxr.store.web.common.dto.RestResponse;
import com.rxr.store.wechat.model.WechatAuth;
import com.rxr.store.wechat.model.menu.Menu;
import com.rxr.store.wechat.service.WechatAuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 无需登录验证的
 */
@RestController
@RequestMapping("/wechat/auth")
public class WechatAuthController {
    @Autowired
    private WechatAuthService wechatAuthService;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder stb = new StringBuilder();
            String xmlHead = "";
            String xmlContent = "";
            String line = null;
            while ((line = in.readLine()) != null) {
                stb.append(line);
            }
            System.out.println(stb.toString());

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
     * 微信获取授权Code
     *
     */
    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public void getAuthCode(HttpServletResponse response,
                            @RequestParam("redirect_uri") String redirectUri)
            throws Exception{
        response.sendRedirect(redirectUri+"/?code=123stit");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public RestResponse login(@RequestParam("code") String code) {
        String wechatId = "abcde92340wer";
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()) {
            Agency agency = wechatAuthService.findAgencyByWechatId(wechatId);
            if(agency == null) {
                agency = new Agency();
                agency.setWechatId(wechatId);
                agency.setName("agency");
            }
            JWTToken jwtToken = new JWTToken(JWTHelper.createToken(agency.getWechatId()));
            subject.login(jwtToken);
        }
        return RestResponse.success(subject.getPrincipal());
    }
}
