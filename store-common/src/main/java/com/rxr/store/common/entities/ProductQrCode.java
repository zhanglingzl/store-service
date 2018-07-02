package com.rxr.store.common.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 商品二维码表
 */
@Entity
@Getter
@Setter
@Table(name = "rxr_product_qr_code")
public class ProductQrCode extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    /**二维码类型 0:支,1:盒,2：箱*/
    @Column(name = "pqc_type")
    private Integer type;
    /**
     * 商品唯一标识码
     * SN码建议使用datacode（产生日期）+产品类目编号+生产批次+生产序列号
     * 比如1525-003-07-002321，即2015年25周生产，
     * 003代表读卡器（这个由企业自行定议），07（第7批次），第2321台
     */
    @Column(name = "pqc_serial_number")
    private String serialNo;
    /**父ID*/
    @Column(name = "pqc_parent_serial_number")
    private String parentSerialNo;
    /**加密后的商品唯一标识码*/
    @Column(name = "pqc_sec_serial_number")
    private String securitySerialNo;
    /**二维码保存路径*/
    @Column(name = "pqc_url")
    private String qrCodeUrl;

}
