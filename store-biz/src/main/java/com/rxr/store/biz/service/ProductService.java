package com.rxr.store.biz.service;

import com.rxr.store.common.entity.Product;
import com.rxr.store.common.form.ProductForm;
import com.rxr.store.common.form.ProductQrCodeForm;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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

    /**
     * 按照箱数新增库存
     * @param qrCodeForm 生成库存
     */
    void createProductQrCode(ProductQrCodeForm qrCodeForm);

    /***
     * 根据商品编号查询商品
     * @param productNo
     * @return
     */
    Product findProductByProductNo(String productNo);

    /**
     * 根据商品ID删除商品
     * @param id 商品ID
     */
    void deleteProductById(Long id);

    /**
     * 保存商品封面及图片
     * @param productNo 商品编号
     * @param multipartRequest 文件request
     */
    void saveProductImage(String productNo, MultipartHttpServletRequest multipartRequest);
}
