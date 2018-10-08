package com.rxr.store.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zero
 * @date Create in 2018-06-10 13:15
 */

@Data
@Entity
@Table(name = "rxr_product")
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity{

    @Column(name = "product_no", nullable = false, unique = true)
    private String productNo;
    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 商品作用
     */
    @Column(name = "product_effect")
    private String effect;
    /**
     * 商品成分
     */
    @Column(name = "product_ingredient")
    private String ingredient;
    /**
     * 商品信息
     */
    @Column(name = "product_description")
    private String description;
    /**
     * 质检报告
     */
    @Column(name = "product_quality_report")
    private String qualityReport;
    /**
     * 产品封面
     */
    @Column(name = "product_cover")
    private String cover;
    /**零售价*/
    @Column(name="product_amount")
    private BigDecimal amount;
    /**会员价*/
    @Column(name="product_vip_amount")
    private BigDecimal vipAmount;

    /**
     * 产品图片
     */
    @Column(name="product_images")
    private String images;
    @ManyToMany
    @JoinTable(name = "agency_level_product",
               joinColumns = {@JoinColumn(name = "product_id")},
               inverseJoinColumns = {@JoinColumn(name = "alp_id")})
    private List<AgencyLevelProduct> agencyLevelProducts;

    @OneToMany(mappedBy = "product", fetch=FetchType.EAGER)
    private List<ProductQrCode> productQrCodes;

    /**
     * 图片访问url
     */
    @Transient
    private String imageUrl;

    /**
     * 删除状态 0:为删除 1：删除
     */
    @Column(name="product_delete_status")
    private Integer deleteStatus = 0;

}
