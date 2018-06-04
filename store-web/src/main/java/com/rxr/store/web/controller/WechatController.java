package com.rxr.store.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.rxr.store.common.entities.Agency;
import com.rxr.store.web.common.dto.RestResponse;
import com.rxr.store.wechat.model.WechatAuth;
import com.rxr.store.wechat.model.menu.Menu;
import com.rxr.store.wechat.service.WechatAuthService;
import com.rxr.store.wechat.util.AuthUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Log
@RestController
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
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

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
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

    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public RestResponse<String> createQRCode() {

        return null;
    }

    @RequestMapping(value = "/createMenu", method = RequestMethod.GET)
    public RestResponse<String> createMenu() throws Exception{
        this.wechatAuthService.createMenu(new Menu());
        return RestResponse.success();
    }

    /**
     * 微信预授权获取授权Code
     * @return
     */
    @RequestMapping(value = "/auth/code", method = RequestMethod.GET)
    public void getAuthCode(HttpServletResponse response,
                                            @RequestParam("redirect_uri") String redirectUri)
            throws Exception{
        String url = AuthUtil.AUTH_CODE_URL.replace("APPID", AuthUtil.APP_ID)
                .replace("REDIRECT_URI","http://store.vicp.la/callback/wechat_code")
                .replace("SCOPE","snsapi_userinfo");
        response.sendRedirect(url);
    }
}
