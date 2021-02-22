package com.hcframe.base.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    /**
     * 日期格式化成字符串
     * @param date
     * @param format
     * @return
     */
    public static String DateFormat(Date date,String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime date1 = date2LocalDateTime(date);
        return date1.format(dtf);
    }

    /**
     * 字符串格式化成日期
     */
    public static Date StringFormat(String string,String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(string,dtf);
        return localDate2Date(localDate);
    }


    /**
     * Date转换为LocalDateTime
     * @param date
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        ZoneId zoneId = ZoneId.systemDefault();//A time-zone ID, such as {@code Europe/Paris}.(时区)
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTime2Date( LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        return Date.from(zdt.toInstant());
    }

    /**
     * localDate转Date
     */
    public static Date localDate2Date(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant1 = zonedDateTime.toInstant();
        return Date.from(instant1);
    }

    /**
     * Date 转 localDate
     */
    public static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        return zdt.toLocalDate();
    }

    /**
     * 获取字符串中的月份
     * @param str
     * @param format
     * @return
     */
    public static int getStringMonth(String str,String format) {
        Date date=StringFormat(str, format);
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

}
