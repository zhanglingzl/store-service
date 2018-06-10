package com.rxr.store.common.enums;

import lombok.Getter;

/**
 * @author zero
 * @date Create in 2018-06-10 11:01
 */
@Getter
public enum  AgencyEnum {

    /**
     * 最大等级值
     */
    MAX_LEVEL(5),
    /**
     * 在职代理
     */
    JOB_AGENCY(0),
    /**
     * 准代理
     */
    QUASI_AGENCY(1),
    /**
     * 准代理
     */
    GUEST_AGENCY(2),

    /**
     * 升级或成为代理确认
     */
    ANSWER_VERIFY(1),
    /**
     * 升级或成为代理确认
     */
    ANSWER_UNCONFIRMED(0);

    private int value;
    AgencyEnum(int value){
        this.value = value;
    }

}
