package com.rxr.store.common.util;

import java.math.BigDecimal;

/**
 * BigDecimal工具类
 *
 * @author ZL
 * @date 2018-10-22 15:18
 **/
public class BigDecimalHelper {

    /**
     * 小数点精度
     */
    public static final int SCALE = 2;

    /**
     * 四舍五入保留两位小数并转换为字符串
     *
     * @param b bigDecimal
     * @return 转换后的字符串
     */
    public static String formatAndRoundToStr(BigDecimal b){
        BigDecimal bigDecimal = b.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
        return String.valueOf(bigDecimal);
    }

    /**
     *  四舍五入保留自定义小数并转换为字符串
     *
     * @param b bigDecimal
     * @param scale 精度
     * @return 转换后的字符串
     */
    public static String formatAndRoundToStr(BigDecimal b, int scale){
        BigDecimal bigDecimal = b.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return String.valueOf(bigDecimal);
    }

    /**
     * 四舍五入保留两位小数
     *
     * @param b bigDecimal
     * @return 转换后的字符串
     */
    public static BigDecimal formatAndRound(BigDecimal b){
        return b.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *  四舍五入保留自定义小数
     *
     * @param b bigDecimal
     * @param scale 精度
     * @return 转换后的字符串
     */
    public static BigDecimal formatAndRound(BigDecimal b, int scale){
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

}
