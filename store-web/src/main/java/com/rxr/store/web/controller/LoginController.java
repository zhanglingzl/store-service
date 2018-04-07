package com.rxr.store.web.controller;

import com.rxr.store.web.common.RestResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public RestResponse login(@Param("userName") String userName, @Param("password") String password){

        RestResponse restResponse = new RestResponse();

        Map<String,Object> result = new HashMap<>(10);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        // 判断当前用户是否登录，未登录执行认证
        if(!subject.isAuthenticated()){
            try {
                subject.login(token);
                result.put("token", subject.getSession().getId());
                restResponse.setResult(result);
                restResponse.setCode(0);
                restResponse.setMsg("登录成功");
            } catch (IncorrectCredentialsException e) {
                restResponse.setCode(1);
                restResponse.setMsg("密码错误");
            } catch (LockedAccountException e) {
                restResponse.setCode(2);
                restResponse.setMsg("登录失败，该用户已被冻结");
            } catch (AuthenticationException e) {
                restResponse.setCode(3);
                restResponse.setMsg("该用户不存在");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            restResponse.setCode(4);
            restResponse.setMsg("The current user is logged in");
        }
        return restResponse;
    }

}
