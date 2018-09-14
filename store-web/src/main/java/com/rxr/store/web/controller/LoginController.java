package com.rxr.store.web.controller;

import com.rxr.store.biz.service.UserService;
import com.rxr.store.common.entity.User;
import com.rxr.store.core.JWTToken;
import com.rxr.store.core.util.JWTHelper;
import com.rxr.store.core.util.PasswordHelper;
import com.rxr.store.web.common.dto.RestResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RestResponse login(@Param("userName") String userName, @Param("password") String password){
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()) {
            User currUser = userService.findUserByLoginName(userName);
            if(currUser== null) {
                throw new UnauthorizedException();
            }
            User user = new User(userName,password,currUser.getSalt());
            PasswordHelper.getSecurePassword(user);
            if(!user.getPassword().equalsIgnoreCase(currUser.getPassword())) {
                throw new UnauthorizedException();
            }
            JWTToken jwtToken = new JWTToken(JWTHelper.createToken(currUser.getLoginName()
                    ,currUser.getPassword()));
            subject.login(jwtToken);
        }
        return RestResponse.success(subject.getPrincipal());
    }

}
