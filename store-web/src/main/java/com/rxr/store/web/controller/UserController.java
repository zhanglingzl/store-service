package com.rxr.store.web.controller;

import com.rxr.store.biz.service.UserService;
import com.rxr.store.common.model.User;
import com.rxr.store.web.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public RestResponse<User> getUser(@PathVariable("id") Integer userId) {
        List<User> users = userService.getAllUser();
        return RestResponse.success(users.get(userId));
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public RestResponse<List<User>> getUser() {
        List<User> users = userService.getAllUser();
        return RestResponse.success(users);
    }

    @RequestMapping(value ="/test")
    public String test(){
        return "test";
    }
}
