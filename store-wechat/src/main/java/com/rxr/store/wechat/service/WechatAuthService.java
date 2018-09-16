package com.rxr.store.wechat.service;

import com.rxr.store.common.dto.WechatJSPay;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.wechat.model.WechatConfig;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.wechat.model.Message;
import com.rxr.store.wechat.model.WechatAuth;
import com.rxr.store.wechat.model.menu.Menu;
import org.dom4j.Document;

public interface WechatAuthService {
    boolean checkSignature(WechatAuth auth);
    Message createMenu(Menu menu) throws Exception;
    Agency findAgencyByWechatId(String wechatId);

    /**
     * 通过授权号获取微信相关信息
     * @param code
     * @return
     */
    Agency findAgencyByCode(String code);

    Agency findAgencyById(Long id);

    String findQrCodeTicket(Long id);

    /**
     * 保存代理账户
     * @param agency
     */
    Agency saveAgency(Agency agency);

    /**
     * 获取微信jssdk数据
     * @return
     * @param referer
     */
    WechatConfig getWechatConfig(String referer);

    /**
     * 微信支付
     * @param trade
     * @return
     */
    WechatJSPay wechatJSPay(Trade trade);

    /***
     * 保存微信支付信息
     * @param document
     */
    void savePayByWechatNotify(Document document);
}
