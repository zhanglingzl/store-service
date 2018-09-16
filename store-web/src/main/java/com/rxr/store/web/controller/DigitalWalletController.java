package com.rxr.store.web.controller;

import com.rxr.store.biz.service.DigitalWalletService;
import com.rxr.store.common.dto.WalletDTO;
import com.rxr.store.common.form.WalletForm;
import com.rxr.store.common.util.NumberHelper;
import com.rxr.store.web.common.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

;

@RestController
public class DigitalWalletController {
    private final DigitalWalletService digitalWalletService;

    @Autowired
    public DigitalWalletController(DigitalWalletService digitalWalletService) {
        this.digitalWalletService = digitalWalletService;
    }

    @GetMapping("/wallet/show/detail")
    public RestResponse<WalletDTO> getWalletDT(WalletForm walletForm) {
        WalletDTO walletDTO = digitalWalletService.getWalletInfo(walletForm);
        return RestResponse.success(walletDTO);
    }

    @GetMapping("/wallet/semiannual/trade-amount")
    public RestResponse<Double[]> getSemiannualAmount(WalletForm walletForm) {
        Double[] amounts = digitalWalletService.getSemiannualAmount(walletForm);
        for (int i=0; i< amounts.length; i++) {
            amounts[i] = NumberHelper.divide(amounts[i], 10000D);
        }
        return RestResponse.success(amounts);

    }
}
