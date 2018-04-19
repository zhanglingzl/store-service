package com.rxr.store.core.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateHelper {

    /**
     *
     * @param localDateTime 计算时间
     * @param addSecond 增加几分钟
     * @return 增加后的时间
     */
    public static Date plusSecond(LocalDateTime localDateTime, long addSecond) {
        if(localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.plusMinutes(addSecond).atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     *
     * @param addSecond 增加几分钟
     * @return 增加后的时间
     */
    public static Date plusSecond(long addSecond) {
        return plusSecond(null, addSecond);
    }

    public static void main(String[] args) {
        System.out.println("aaa" + plusSecond(5));
    }
}
