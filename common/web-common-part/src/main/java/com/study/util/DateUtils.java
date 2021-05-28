package com.study.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static final String DATE_FORMAT_YYYY = "yyyy";

    public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    public static final String DATE_FORMAT_YYMMDD = "yyMMdd";

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_FORMAT_DD_MM_YYYY = "dd MM yyyy";

    public static final String DATE_FORMAT_DDMMYYYY = "ddMMyyyy";

    public static final String DATE_FORMAT_HHMMSS = "HH:mm:ss";

    public static final String DATE_FORMAT_HHMMSS2 = "HHmmss";

    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMM = "yyyyMMddHHmm";

    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_DDMMYYYYHHMMSS = "ddMMyyyyHHmmss";

    public static final String DATE_FORMAT_DDMMYYYYHHMM = "ddMMyyyyHHmm";

    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS1 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS2 = "yyyy/MM/dd HH:mm:ss";

    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy/MM/dd HH:mm";

    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS3 = "dd/MM/yyyy HH:mm:ss";

    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS4 = "MM/dd/yyyy HH:mm:ss";

    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS5 = "dd-MM-yyyy HH:mm:ss";

    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_1 = "dd-MM-yyyy HH:mm";

    public static final String DATE_TIME_FORMAT_d_MMM_yyyy = "MMM dd,yyyy";

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String asDateTimeStr(Date date, String formatter) {
        LocalDateTime localDateTime = asLocalDateTime(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        return localDateTime.format(dateTimeFormatter);
    }
}
