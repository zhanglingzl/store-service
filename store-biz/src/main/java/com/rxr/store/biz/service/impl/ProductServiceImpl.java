package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.ProductRepository;
import com.rxr.store.biz.service.ProductService;
import com.rxr.store.common.entities.Product;
import com.rxr.store.common.form.ProductForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * @author zero
 * @date Create in 2018-06-13 22:10
 */
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> getProducts(ProductForm productForm) {
        List<Product> productList = repository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(StringUtils.isNotBlank(productForm.getProductName())){
                predicate.getExpressions().add(criteriaBuilder.like(root.get("productName"), "%" + productForm.getProductName() + "%"));
            }
            return predicate;
        });
        return productList;
    }

    @Override
    public void saveOrUpdateProduct(Product product) {
        repository.save(product);
    }
}
