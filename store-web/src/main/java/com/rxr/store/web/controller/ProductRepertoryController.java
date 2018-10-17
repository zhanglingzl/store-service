package com.rxr.store.web.controller;

import com.rxr.store.biz.service.ProductRepertoryService;
import com.rxr.store.common.entity.Product;
import com.rxr.store.common.entity.ProductRepertory;
import com.rxr.store.common.form.ProductRepertoryForm;
import com.rxr.store.web.common.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZL
 * @date 2018-10-10 21:47
 **/
@RestController
public class ProductRepertoryController {

    @Autowired
    private ProductRepertoryService service;

    @GetMapping("/repertory")
    public RestResponse<List<ProductRepertory>> getProductRepertory(ProductRepertoryForm productRepertoryForm){
        return RestResponse.success(service.getProductRepertory(productRepertoryForm));
    }

    @GetMapping("/repertory/product")
    public RestResponse<List<Product>> getProduct(ProductRepertoryForm productRepertoryForm){
        return RestResponse.success(service.getProduct(productRepertoryForm));
    }
}
