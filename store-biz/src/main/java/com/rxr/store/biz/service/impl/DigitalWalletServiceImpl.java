package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.DigitalWalletRepository;
import com.rxr.store.biz.service.AgencyService;
import com.rxr.store.biz.service.DigitalWalletService;
import com.rxr.store.biz.service.TradeService;
import com.rxr.store.common.dto.WalletDTO;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.DigitalWallet;
import com.rxr.store.common.form.WalletForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DigitalWalletServiceImpl implements DigitalWalletService {
    private final TradeService tradeService;
    private final DigitalWalletRepository digitalWalletRepository;
    private final AgencyService agencyService;
    @Autowired
    public DigitalWalletServiceImpl(TradeService tradeService, DigitalWalletRepository digitalWalletRepository,
                                    AgencyService agencyService) {
        this.tradeService = tradeService;

        this.digitalWalletRepository = digitalWalletRepository;
        this.agencyService = agencyService;
    }

    @Override
    public void saveWallet(DigitalWallet digitalWallet) {
        this.digitalWalletRepository.save(digitalWallet);
    }

    @Override
    public WalletDTO getWalletInfo(WalletForm walletForm) {
        WalletDTO wallet = new WalletDTO();
        Agency agency = this.agencyService.findAgencyById(walletForm.getAgencyId());
        DigitalWallet digitalWallet = digitalWalletRepository.findDigitalWalletByAgency(agency);
        List<Agency> agencies = new ArrayList<>();
        agencies.add(agency);
        Double[] tradeAmount = tradeService.getSemiannualTrade(agencies, 5);
                wallet.setDigitalWallet(digitalWallet);
        wallet.setTradeAmount(tradeAmount);
        List<Agency> firstAgencies = this.agencyService.listAgenciesByParentIds(agencies.stream()
                .map(Agency::getId).collect(Collectors.toList()));
        Double[] firstTradeAmount = tradeService.getSemiannualTrade(firstAgencies, 1);
        wallet.setFirstTradeAmount(firstTradeAmount);
        if(firstAgencies.size() > 0) {
            List<Agency> secondAgencies = this.agencyService.listAgenciesByParentIds(firstAgencies
                    .stream().map(Agency::getId).collect(Collectors.toList()));
            Double[] secondTradeAmount = this.tradeService.getSemiannualTrade(secondAgencies, 1);
            wallet.setSecondTradeAmount(secondTradeAmount);
            List<Agency> thirdAgencies = this.agencyService.listAgenciesByParentIds(secondAgencies.stream()
                    .map(Agency::getId).collect(Collectors.toList()));
            if(thirdAgencies.size() > 0) {
                Double[] thirdTradeAmount = this.tradeService.getSemiannualTrade(thirdAgencies, 1);
                wallet.setThirdTradeAmount(thirdTradeAmount);
            } else {
                wallet.setThirdTradeAmount(new Double[] {0d, 0d});
            }

        } else {
            wallet.setSecondTradeAmount(new Double[] {0d, 0d});
            wallet.setThirdTradeAmount(new Double[] {0d, 0d});
        }

        return wallet;
    }

    @Override
    public Double[] getSemiannualAmount(WalletForm walletForm) {
        Agency agency = this.agencyService.findAgencyById(walletForm.getAgencyId());
        Double[] tradeAmount = tradeService.getSemiannualTrade(Arrays.asList(agency), 5);
        return tradeAmount;
    }
}
