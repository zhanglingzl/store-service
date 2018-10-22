package com.rxr.store.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
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
        Instant instant = localDateTime.plusSeconds(addSecond).atZone(zone).toInstant();
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
     *增加分钟数
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
        return dateToLocalDateTime(date).isBefore(LocalDateTime.now());
    }

    public static boolean isBefore(Date before, Date end) {
        return dateToLocalDateTime(before).isBefore(dateToLocalDateTime(end));
    }

    public static String serialDateCode() {
        LocalDate localDate = LocalDate.now();
        String year = String.valueOf(localDate.getYear()).substring(2,4);
        String week = String.valueOf(localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        return year+week;
    }

    /**
     * localDateTime转换为格式化时间
     * @param date date
     * @param pattern 格式
     * @return
     */
    public static String format(Date date, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.format(formatter);
    }

    public static Date format(String date, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime.parse(date, df);
        return localDateTimeToDate(LocalDateTime.parse(date, df));
    }
    public static Date getFirstDayofMounth(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime firstDay = localDateTime.with(TemporalAdjusters.firstDayOfMonth());
        firstDay = LocalDateTime.of(firstDay.toLocalDate(),LocalTime.of(0,0,0));
        return localDateTimeToDate(firstDay);
    }
    public static Date getLastDayofMounth(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime lastDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
        lastDay = LocalDateTime.of(lastDay.toLocalDate(),LocalTime.of(23,59,59));
        return localDateTimeToDate(lastDay);
    }
    public static Date getFirstDayofMounth() {
        return getFirstDayofMounth(null);
    }

    public static Date getLastDayofMounth() {
        return getLastDayofMounth(null);
    }

    public static Date minusMonths(Date date, int month) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime temp = LocalDateTime.of(localDateTime.minusMonths(month).toLocalDate(),localDateTime.toLocalTime());
        return localDateTimeToDate(temp);
    }

    public static Date minusMinutes(Date date, int minutes) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime temp = LocalDateTime.of(localDateTime.toLocalDate(),localDateTime.minusMinutes(minutes).toLocalTime());
        return localDateTimeToDate(temp);
    }

    public static Date plusMonths(Date date, int month) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime temp = LocalDateTime.of(localDateTime.plusMonths(month).toLocalDate(),localDateTime.toLocalTime());
        return localDateTimeToDate(temp);
    }
        public static void main(String[] args) {
        Date date = new Date();
        Date date1 = minusMinutes(date, 5);
            System.out.println(DateHelper.isBefore(date1));
        System.out.println(format(date1,"yyyy-MM-dd HH:mm:ss"));
        Date date2 = getLastDayofMounth();
        Date date3 = minusMinutes(date, 30);
        System.out.println(format(date2,"yyyy-MM-dd HH:mm:ss"));
            System.out.println(format(date3,"yyyy-MM-dd HH:mm:ss"));
    }


}
