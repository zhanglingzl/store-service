package com.rxr.store.web.controller;

import com.rxr.store.biz.service.ProductService;
import com.rxr.store.common.entity.Product;
import com.rxr.store.common.form.ProductForm;
import com.rxr.store.common.form.ProductQrCodeForm;
import com.rxr.store.web.common.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public RestResponse saveProduct(@RequestBody Product product){
        productService.saveOrUpdateProduct(product);
        return RestResponse.success();
    }

    @PostMapping("/product/createProductQrCode")
    public RestResponse createProductQrCode(ProductQrCodeForm qrCodeForm) {
        productService.createProductQrCode(qrCodeForm);
        return RestResponse.success();
    }

    @GetMapping("/product/{productNo}")
    public RestResponse<Product> findProductByProductNo(@PathVariable("productNo") String productNo) {
        Product product = this.productService.findProductByProductNo(productNo);
        return RestResponse.success(product);
    }

    @DeleteMapping("/product/delete")
    public RestResponse deleteProductById(Long id){
        productService.deleteProductById(id);
        return RestResponse.success();
    }

}
