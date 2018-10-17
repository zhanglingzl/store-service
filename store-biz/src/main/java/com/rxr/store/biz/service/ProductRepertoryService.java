package com.rxr.store.biz.service;

import com.rxr.store.common.entity.Product;
import com.rxr.store.common.entity.ProductRepertory;
import com.rxr.store.common.form.ProductRepertoryForm;

import java.util.List;

/**
 * @author ZL
 * @date 2018-10-10 21:52
 **/
public interface ProductRepertoryService {

    /**
     * 根据查询条件获取商品库存信息
     *
     * @param productRepertoryForm 查询form
     * @return 商品库存列表
     */
    List<ProductRepertory>  getProductRepertory(ProductRepertoryForm productRepertoryForm);

    /**
     * 根据查询条件获取商品信息
     *
     * @param productRepertoryForm 查询form
     * @return 商品列表
     */
    List<Product>  getProduct(ProductRepertoryForm productRepertoryForm);

}
