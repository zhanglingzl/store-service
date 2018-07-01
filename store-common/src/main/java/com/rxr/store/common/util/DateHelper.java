package com.rxr.store.common.util;

import java.time.*;
import java.util.Date;

/**
 * 时间辅助类
 */
public class DateHelper {

    /**
     *
     * @param localDateTime 计算时间
     * @param addSecond 增加秒数
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
     * @param second 增加几分钟
     * @return 增加后的时间
     */
    public static Date plusSecond(long second) {
        return plusSecond(null, second);
    }

    /**
     *
     * @param localDateTime 时间
     * @param minutes 增加分钟数
     * @return
     */
    public static Date plusMinutes(LocalDateTime localDateTime, long minutes) {
        if(localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.plusMinutes(minutes).atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     *
     * @param minutes zen
     * @return
     */
    public static Date plusMinutes(long minutes) {
        return plusMinutes(null, minutes);
    }

    /**
     *
     * @param date 时间
     * @return 转换 LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if(date == null) {
            date = new Date();
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     *  当前时间转换为 LocalDateTime
     * @return
     */
    public static LocalDateTime dateToLocalDateTime() {
        return dateToLocalDateTime(null);
    }

    /**
     *  localDateTime 转换为Date
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if(localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * localDateTime 转换为Date
     * @return
     */
    public static Date localDateTimeToDate() {
        return localDateTimeToDate(null);
    }

    /**
     *
     * @param date 日期
     * @return 判断该时间是否小于当前时间
     */
    public static boolean isBefore(Date date) {
        return dateToLocalDateTime(date).isBefore(LocalDateTime.now(Clock.systemUTC()));
    }

    public static void main(String[] args) {
       Date date = new Date();
       System.out.println(date);
       date = plusMinutes(-7200);
       System.out.println(date);
       System.out.println(isBefore(date));

    }
}
