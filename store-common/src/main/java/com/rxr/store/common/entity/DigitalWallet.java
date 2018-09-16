package com.rxr.store.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;


@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class DigitalWallet extends BaseEntity {
    /**入账总金额*/
    private BigDecimal totalAmount = BigDecimal.ZERO;
    /**余额*/
    private BigDecimal balance = BigDecimal.ZERO;
    /**提现金额*/
    private BigDecimal withdrawalAmount = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
}
