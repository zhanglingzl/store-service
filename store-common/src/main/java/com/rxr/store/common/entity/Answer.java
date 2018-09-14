package com.rxr.store.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author zero
 * @date Create in 2018-06-09 12:49
 */

@Data
@Entity
@Table(name = "rxr_answer")
@EqualsAndHashCode(callSuper = true)
public class Answer extends BaseEntity {

    /**
     * 答题分数
     */
    @Column(name = "an_score")
    private Integer score;

    /**
     * 答题状态 0:未审核 1：审核通过
     */
    @Column(name = "an_status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

}
