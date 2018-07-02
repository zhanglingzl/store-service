package com.rxr.store.web.controller;

import com.rxr.store.biz.service.ProductService;
import com.rxr.store.common.entities.Product;
import com.rxr.store.common.form.ProductForm;
import com.rxr.store.web.common.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zero
 * @date Create in 2018-06-10 12:09
 */
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public RestResponse<List<Product>> getProducts(ProductForm productForm){
        return RestResponse.success(productService.getProducts(productForm));
    }

    @PostMapping("/product/saveOrUpdate")
    public RestResponse saveProduct(Product product){
        productService.saveOrUpdateProduct(product);
        return RestResponse.success();
    }

    @PostMapping("/product/createQrCode")
    public RestResponse createQrCode() {
        productService.createQrCode(40);
        return RestResponse.success();
    }
}
