package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author zero
 * @date Create in 2018-06-12 21:10
 */
@Getter
@Setter
@Entity
@Table(name = "rxr_agency_level_product")
public class AgencyLevelProduct extends BaseEntity{

    /**
     * 代理等级
     */
    @Column(name = "alp_level")
    private Integer level;

    /**
     * 初次进货
     */
    @Column(name = "alp_first_stockCount")
    private Integer firstStockCount;

    /**
     * 补货最小值
     */
    @Column(name = "alp_replenish_minCount")
    private Integer replenishMinCount;

    @ManyToMany
    @JoinTable(name = "agency_level_product",
               joinColumns = {@JoinColumn(name = "alp_id")},
               inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private List<Product> products;
}
