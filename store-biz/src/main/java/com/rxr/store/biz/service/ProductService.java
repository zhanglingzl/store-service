package com.rxr.store.biz.service;

import com.rxr.store.common.entities.Product;
import com.rxr.store.common.form.ProductForm;

import java.util.List;

/**
 * @author zero
 * @date Create in 2018-06-13 22:07
 */
public interface ProductService {

    /**
     * 获取产品信息
     * @param productForm
     * @return
     */
    List<Product> getProducts(ProductForm productForm);

    void saveOrUpdateProduct(Product product);
}
