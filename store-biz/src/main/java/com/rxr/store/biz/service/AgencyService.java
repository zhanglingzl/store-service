package com.rxr.store.biz.service;

import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.dto.AgencyDto;
import com.rxr.store.common.form.AgencyForm;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Pair<List<Long>, int[]> findAgencyByCount(Agency agency);

    Page<Agency> listAgencies(AgencyForm type, Pageable pageable);

    Agency findAgencyById(Long agencyId);

    List<Agency> listAgenciesByParentIds(List<Long> parentIds);

    void updateAgency(AgencyForm agencyForm);
}
