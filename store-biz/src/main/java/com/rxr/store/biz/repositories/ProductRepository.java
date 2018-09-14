package com.rxr.store.biz.repositories;

import com.rxr.store.common.entity.Product;

/**
 * @author zero
 * @date Create in 2018-06-13 22:38
 */
public interface ProductRepository extends BaseRepository<Product, Long> {
    Product findByProductNo(String productNo);
}
