package com.rxr.store.web.controller;

import com.rxr.store.web.common.dto.RestResponse;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/wechat")
public class WechatController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public RestResponse test() {
        System.out.println(12345);
        return RestResponse.success();
    }


}
