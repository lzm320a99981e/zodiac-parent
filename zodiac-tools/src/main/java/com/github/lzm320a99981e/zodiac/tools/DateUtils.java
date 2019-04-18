package com.github.lzm320a99981e.zodiac.tools;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * 日期工具类
 */
public abstract class DateUtils {
    /**
     * 解析日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date, String pattern) {
        return DateTime.parse(date, DateTimeFormat.forPattern(pattern)).toDate();
    }

    /**
     * 格式化日期对象
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return new DateTime(date).toString(pattern);
    }

    /**
     * 格式化当前日期对象
     *
     * @param pattern
     * @return
     */
    public static String formatNow(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 使用 yyyy-MM-dd 格式进行格式化
     *
     * @param date
     * @return
     */
    public static String formatUseYmd(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 使用 HH:mm:ss 格式进行格式化
     *
     * @param date
     * @return
     */
    public static String formatUseHms(Date date) {
        return format(date, "HH:mm:ss");
    }

    /**
     * 使用 yyyy-MM-dd HH:mm:ss 格式进行格式化
     *
     * @param date
     * @return
     */
    public static String formatUseYmdHms(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }


}


