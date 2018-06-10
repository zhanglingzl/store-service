package com.rxr.store.biz.service;

import com.rxr.store.common.entities.Agency;
import com.rxr.store.common.dto.AgencyDto;
import com.rxr.store.common.form.AgencyForm;

import java.util.List;

/**
 * @author zero
 * @date Create in 2018/6/2 14:43
 */
public interface AgencyService {

    /**
     * 获取代理信息
     * @param agencyForm
     * @return
     */
    List<AgencyDto> getAgencise(AgencyForm agencyForm);

    /**
     * 获取准代理信息
     * @param agencyForm
     * @return
     */
    List<AgencyDto> getQuasiAgencies(AgencyForm agencyForm, int status);

    Agency findAgencyByWechatId(String wechatId);

    /**
     * 确认成为代理
     */
    void verifyAgency(Long id);

    /**
     * 确认升级
     */
    void verifyUpdate(Long id);

}
