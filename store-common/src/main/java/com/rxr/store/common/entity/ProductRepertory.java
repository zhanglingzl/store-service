package com.rxr.store.common.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 商品库存
 *
 * @author ZL
 * @date 2018-10-10 21:29
 **/
@Entity
@Table(name = "rxr_product_repertory")
@Data
public class ProductRepertory extends BaseEntity{

    /**
     * 商品二维码
     */
    @Column(name = "qr_code")
    private String qrCode;

    /**
     * 商品状态 0: 入库, 1:出库
     */
    @Column(name = "repertory_status")
    private int status = 0;

    /**
     * 商品
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
