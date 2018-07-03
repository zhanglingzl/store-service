package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author zero
 * @date Create in 2018-06-10 13:15
 */
@Getter
@Setter
@Entity
@Table(name = "rxr_product")
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
     * 商品规格，0：件 1：个 2：箱
     */
    @Column(name = "product_specification")
    private Integer specification;

    @ManyToMany
    @JoinTable(name = "agency_level_product",
               joinColumns = {@JoinColumn(name = "product_id")},
               inverseJoinColumns = {@JoinColumn(name = "alp_id")})
    private List<AgencyLevelProduct> agencyLevelProducts;

    @OneToMany(mappedBy = "product", fetch=FetchType.EAGER)
    private List<ProductQrCode> productQrCodes;

}
