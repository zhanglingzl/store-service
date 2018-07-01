package com.rxr.store.wechat.service;

import com.rxr.store.common.entities.Agency;
import com.rxr.store.wechat.model.Message;
import com.rxr.store.wechat.model.WechatAuth;
import com.rxr.store.wechat.model.menu.Menu;

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

    Agency findParentAgencyById(Long parentId);

    String findQrCodeTicket(Long id);
}
