package fun.timu.cloud.net.app.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {

    /**
     * 默认日期格式
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    private static final String DEFAULT_PATTERN_WITH_TIME = "yyyy-MM-dd hh:mm:ss";

    /**
     * 默认日期格式
     */
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    /**
     * 默认日期时间格式
     */
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_WITH_TIME);


    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();


    /**
     * 将 LocalDateTime 对象转换为指定格式的字符串
     * 此方法用于格式化 LocalDateTime 对象，使其以指定的日期和时间格式输出
     *
     * @param localDateTime 需要格式化的 LocalDateTime 对象
     * @param pattern       日期和时间的格式模式，例如 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的日期和时间字符串
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        // 创建一个 DateTimeFormatter 实例，用于将 LocalDateTime 格式化为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        // 将 LocalDateTime 对象与默认时区结合，然后格式化为字符串
        String timeStr = formatter.format(localDateTime.atZone(DEFAULT_ZONE_ID));
        // 返回格式化后的日期和时间字符串
        return timeStr;
    }


    /**
     * Date 转 字符串, 指定日期格式
     *
     * @param time    日期对象，表示需要格式化的时间
     * @param pattern 日期格式字符串，指定输出的日期格式
     * @return 格式化后的日期字符串
     */
    public static String format(Date time, String pattern) {
        // 创建指定格式的 DateTimeFormatter 对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        // 将 Date 对象转换为 Instant，并结合时区信息格式化为字符串
        String timeStr = formatter.format(time.toInstant().atZone(DEFAULT_ZONE_ID));
        // 返回格式化后的日期字符串
        return timeStr;
    }

    /**
     * 将Date对象转换为字符串，使用默认的日期格式
     *
     * @param time Date对象，表示要转换的时间
     * @return 转换后的字符串表示的时间
     */
    public static String format(Date time) {
        // 使用默认时区和日期格式将Date对象转换为字符串
        String timeStr = DEFAULT_DATE_TIME_FORMATTER.format(time.toInstant().atZone(DEFAULT_ZONE_ID));
        return timeStr;
    }

    /**
     * 将Date对象转换为字符串表示，使用默认的日期格式
     * 此方法主要用于将给定的时间戳转换为人类可读的日期字符串
     * 它使用了默认的日期格式和时区设置
     *
     * @param timestamp 时间戳，表示自1970年1月1日0时0分0秒（UTC）以来的毫秒数
     * @return 转换后的日期字符串
     */
    public static String format(Long timestamp) {
        // 使用默认日期格式和时区将时间戳格式化为日期字符串
        String timeStr = DEFAULT_DATE_FORMATTER.format(new Date(timestamp).toInstant().atZone(DEFAULT_ZONE_ID));
        return timeStr;
    }


    /**
     * 将时间戳转换为指定格式的日期字符串
     * 此方法用于将给定的时间戳转换为一个符合指定日期时间格式的字符串表示形式
     * 主要用途是用于日志记录、事件时间显示等，其中时间格式的一致性至关重要
     *
     * @param timestamp 时间戳，单位为毫秒
     * @return 格式化后的日期时间字符串
     */
    public static String formatWithTime(long timestamp) {
        // 使用默认的日期时间格式器和时区ID将时间戳转换为日期字符串
        String timeStr = DEFAULT_DATE_TIME_FORMATTER.format(new Date(timestamp).toInstant().atZone(DEFAULT_ZONE_ID));
        return timeStr;
    }


    /**
     * 将字符串时间转换为Date对象
     * 此方法用于解析给定格式的日期时间字符串，并将其转换为Date对象
     * 主要用途是用于处理日期时间字符串，以便在Java程序中以Date对象的形式使用
     *
     * @param time 日期时间字符串，应符合DEFAULT_DATE_TIME_FORMATTER所定义的格式
     * @return 解析后的Date对象
     */
    public static Date strToDate(String time) {
        // 解析字符串为LocalDateTime对象
        LocalDateTime localDateTime = LocalDateTime.parse(time, DEFAULT_DATE_TIME_FORMATTER);
        // 将LocalDateTime对象转换为Date对象
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }
}
