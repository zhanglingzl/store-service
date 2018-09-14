package com.rxr.store.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class StringHelper {
    /**流水号加1后返回，流水号长度为6*/
    private static final String STR_FORMAT = "000000";

    public static String numberAddOne(String serialNo){
        Integer intHao = Integer.parseInt(serialNo);
        intHao++;
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        return df.format(intHao);
    }

    /**
     * 生成支付订单号
     */
    public static String getPayTradeNo() {
        return "rxr_" + DateHelper.format(LocalDateTime.now(),"yyyyMMddHHmmss")+"_"
                + RandomStringUtils.random(6, false, true);
    }

    /**
     * 获取客户端IP
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }

}
