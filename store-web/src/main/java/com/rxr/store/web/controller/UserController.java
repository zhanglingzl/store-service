package com.rxr.store.web.controller;

import com.rxr.store.biz.service.UserService;
import com.rxr.store.common.entity.User;
import com.rxr.store.common.form.UserForm;
import com.rxr.store.web.common.dto.PageData;
import com.rxr.store.web.common.dto.PageParams;
import com.rxr.store.web.common.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //@RequestMapping(value = "/user", method = RequestMethod.GET)
    //public RestResponse<List<User>> getUser() {
    //
    //    List<User> users = userService.findAllUser();
    //    return RestResponse.success(users);
    //}

    @RequestMapping(value ="/test")
    public String test(){
        return "test";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public RestResponse<PageData<User>> findAllUser(UserForm user, PageParams pageParams) {
        Page<User> users = userService.findAllUser(user, pageParams.pageable());
        return RestResponse.success(PageData.bulid(users));
    }

    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public RestResponse<User> findUserById(@PathVariable("id") Long id) {
        User user = this.userService.findUserById(id);
        return RestResponse.success(user);
    }
}
