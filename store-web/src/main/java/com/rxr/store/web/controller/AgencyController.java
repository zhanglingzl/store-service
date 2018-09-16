package com.rxr.store.web.controller;

import com.rxr.store.biz.service.AgencyService;
import com.rxr.store.common.dto.AgencyDto;
import com.rxr.store.common.dto.PageParams;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.enums.AgencyEnum;
import com.rxr.store.common.form.AgencyForm;
import com.rxr.store.web.common.dto.PageData;
import com.rxr.store.web.common.dto.RestResponse;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zero
 * @date Create in 2018/6/2 14:43
 */
@RestController
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @GetMapping("/agency")
    public RestResponse<List<AgencyDto>> getAgencies(AgencyForm agencyForm){
        return RestResponse.success(agencyService.getAgencise(agencyForm));
    }

    @GetMapping("/agency/quasi")
    public RestResponse<List<AgencyDto>> getQuasiAgencies(AgencyForm agencyForm){
        return RestResponse.success(agencyService.getQuasiAgencies(agencyForm, AgencyEnum.QUASI_AGENCY.getValue()));
    }

    @GetMapping("/agency/upgrade")
    public RestResponse<List<AgencyDto>> getUpgradeAgencies(AgencyForm agencyForm){
        return RestResponse.success(agencyService.getQuasiAgencies(agencyForm,AgencyEnum.JOB_AGENCY.getValue()));
    }

    @PostMapping("/agency/verify")
    public RestResponse verifyAgency(Long id){
        agencyService.verifyAgency(id);
        return RestResponse.success();
    }

    @PostMapping("/agency/update")
    public RestResponse verifyUpdate(Long id){
        agencyService.verifyUpdate(id);
        return RestResponse.success();
    }

    @GetMapping("/agency/count")
    public RestResponse<int[]> findAgencyCount() {
        Agency agency = (Agency) SecurityUtils.getSubject().getPrincipal();
        return RestResponse.success(this.agencyService.findAgencyByCount(agency).getValue());
    }

    @GetMapping("/agency/list")
    public RestResponse<PageData<Agency>> listAgencies(AgencyForm agencyForm, PageParams pageParams) {
        Agency agency = (Agency) SecurityUtils.getSubject().getPrincipal();
        agencyForm.setId(agency.getId());
        Page<Agency> page = agencyService.listAgencies(agencyForm, pageParams.pageable());
        return RestResponse.success(PageData.bulid(page));
    }

    @GetMapping("/agency/{id}")
    public RestResponse<Agency> findAgencyById(@PathVariable("id") Long agencyId) {
        Agency agency = this.agencyService.findAgencyById(agencyId);
        return RestResponse.success(agency);
    }
    @PutMapping("/agency/edit")
    public RestResponse<Agency> updateAgency(@RequestBody AgencyForm agencyForm) {
        this.agencyService.updateAgency(agencyForm);
        return RestResponse.success();
    }

}
