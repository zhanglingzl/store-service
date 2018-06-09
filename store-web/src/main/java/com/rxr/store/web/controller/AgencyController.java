package com.rxr.store.web.controller;

import com.rxr.store.biz.service.AgencyService;
import com.rxr.store.common.dto.AgencyDto;
import com.rxr.store.common.form.AgencyForm;
import com.rxr.store.web.common.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return RestResponse.success(agencyService.getQuasiAgencies(agencyForm));
    }

}
