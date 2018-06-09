package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class RestTemplateServiceImpl implements RestTemplateService {
    @Autowired
    RestTemplate template;
}
