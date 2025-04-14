package fun.timu.cloud.net.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeUtil {
    /**
     * 默认日期格式
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();


    /**
     * 将 LocalDateTime 对象转换为指定格式的字符串
     * 此方法主要用于将日期和时间以特定格式呈现，提高可读性和适应不同场景的需求
     *
     * @param localDateTime 需要格式化的 LocalDateTime 对象，代表一个具体的日期和时间点
     * @param pattern 日期时间格式模式，用于定义返回字符串的格式
     * @return 根据指定格式转换后的日期时间字符串
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        // 创建一个DateTimeFormatter实例，用于格式化日期时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        // 将LocalDateTime转换为指定时区的ZonedDateTime，并格式化为字符串
        String timeStr = formatter.format(localDateTime.atZone(DEFAULT_ZONE_ID));
        // 返回格式化后的日期时间字符串
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
     * 将时间戳转换为字符串表示的日期和时间
     * 此方法使用默认的日期时间格式和时区进行转换
     *
     * @param timestamp 时间戳，自1970年1月1日0时0分0秒UTC以来的毫秒数
     * @return 日期和时间的字符串表示
     */
    public static String format(long timestamp) {
        // 使用默认日期时间格式和时区将时间戳格式化为字符串
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


    /**
     * 获取当天剩余的秒数,用于流量包过期配置
     * 此方法主要用于计算从当前时间到次日零点之间的秒数，以便在流量包过期等场景中进行计时
     *
     * @param currentDate 当前日期对象，表示当前的时间点
     * @return 返回从当前时间到次日零点之间的秒数
     */
    public static Integer getRemainSecondsOneDay(Date currentDate) {
        // 获取次日零点的时间，用于计算剩余秒数
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // 获取当前时间点，用于计算剩余秒数
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());

        // 计算从当前时间到次日零点的秒数
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }
}
