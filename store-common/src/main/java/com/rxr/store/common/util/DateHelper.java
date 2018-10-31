package com.rxr.store.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 时间辅助类
 */
public class DateHelper {

    public static final String FORMAT_YMD_PATTERN = "yyyy-MM-dd";
    public static final String FORMAT_YM_PATTERN = "yyyy-MM";

    private static DateFormat ymdFormatInstance = null;

    private static DateFormat ymFormatInstance = null;

    public static synchronized DateFormat getYmdFormatInstance() {
        if (null == ymdFormatInstance) {
            ymdFormatInstance = new SimpleDateFormat(FORMAT_YMD_PATTERN);
        }
        return ymdFormatInstance;
    }

    public static synchronized DateFormat getYmFormatInstance() {
        if (null == ymFormatInstance) {
            ymFormatInstance = new SimpleDateFormat(FORMAT_YM_PATTERN);
        }
        return ymFormatInstance;
    }

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
        String year = String.valueOf(localDate.getYear()).substring(2, 4);
        String week = String.valueOf(localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        return year + week;
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
        LocalDateTime temp = LocalDateTime.of(localDateTime.plusMonths(month).toLocalDate(), localDateTime.toLocalTime());
        return localDateTimeToDate(temp);
    }

    /**
     * 获取月第一天.
     * <p>
     * <p><code>step=0</code>获取当月，step为正整数表示获取当前月后几个月第一天，为负数表示当月向前.</p>
     *
     * @param step 步长(整数)
     * @return 某月第一天
     */
    public static LocalDate getMonthFirstDay(long step) {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusMonths(step);
    }

    /**
     * 获取月最后一天.
     * <p>
     * <p><code>step=0</code>获取当月，step为正整数表示获取当前月后几个月第一天，为负数表示当月向前.</p>
     *
     * @param step 步长(整数)
     * @return 某月最后一天
     */
    public static LocalDate getMonthLastDay(long step) {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).plusMonths(step);
    }

    /**
     * 获取月第一天.
     * <p>
     * <p><code>step=0</code>获取当月，step为正整数表示获取当前月后几个月第一天，为负数表示当月向前.</p>
     *
     * @param step 步长(整数)
     * @return 某月第一天
     */
    public static Date getMonthFirstDayToDate(long step) {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusMonths(step);
        return covertLocalToDate(localDate);
    }

    /**
     * 获取月最后一天.
     * <p>
     * <p><code>step=0</code>获取当月，step为正整数表示获取当前月后几个月第一天，为负数表示当月向前.</p>
     *
     * @param step 步长(整数)
     * @return 某月最后一天
     */
    public static Date getMonthLastDayToDate(long step) {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).plusMonths(step);
        return covertLocalToDate(localDate);
    }

    /**
     * 将日期格式化为yyyy-MM-dd 00:00:00
     *
     * @param date 日期
     * @return 转换后的日期
     */
    public static Date convertDate(Date date) {
        try {
            return getYmFormatInstance().parse(getYmFormatInstance().format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date covertLocalToDate(LocalDate localDate){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }
}


