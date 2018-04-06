package com.rxr.store.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @PostMapping("/ajaxLogin")
    public String login(String userName, String password){
        Map<String,Object> result = new HashMap<String,Object>();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        if(!subject.isAuthenticated()){ // 判断当前用户是否登录，未登录执行认证
            try {
                subject.login(token);
                result.put("token", subject.getSession().getId());
                result.put("msg", "登录成功");
            } catch (IncorrectCredentialsException e) {
                result.put("msg", "密码错误");
            } catch (LockedAccountException e) {
                result.put("msg", "登录失败，该用户已被冻结");
            } catch (AuthenticationException e) {
                result.put("msg", "该用户不存在");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            result.put("msg", "The current user is logged in");
        }
        return result.toString();
    }

}
