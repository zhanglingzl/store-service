package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.ProductRepository;
import com.rxr.store.biz.service.ProductRepertoryService;
import com.rxr.store.common.entity.Product;
import com.rxr.store.common.entity.ProductRepertory;
import com.rxr.store.common.form.ProductRepertoryForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * @author ZL
 * @date 2018-10-15 18:49
 **/
@Service
public class ProductRepertoryServiceImpl implements ProductRepertoryService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductRepertory> getProductRepertory(ProductRepertoryForm productRepertoryForm) {
        return null;
    }

    @Override
    public List<Product> getProduct(ProductRepertoryForm productRepertoryForm) {

        return null;
    }
}
