package fun.timu.cloud.net.common.util;


import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.*;

public class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 获取用户真实IP地址，适用于处理通过代理的情况
     * 该方法尝试从请求头中获取IP地址，如果获取失败或IP地址无效，则尝试从其他请求头中获取
     * 如果所有请求头都未能提供有效的IP地址，则返回服务器直接获取的客户端IP地址
     * 在处理通过多个代理的情况时，该方法会提取出第一个有效的IP地址作为用户真实IP地址
     *
     * @param request 用户请求对象，用于获取请求头信息
     * @return 用户真实IP地址，如果无法获取，则返回空字符串
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            // 尝试从请求头中获取IP地址
            ipAddress = request.getHeader("x-forwarded-for");
            // 如果获取失败或IP地址无效，则尝试从其他请求头中获取
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                // 如果所有请求头都未能提供有效的IP地址，则直接获取客户端IP地址
                ipAddress = request.getRemoteAddr();
                // 如果获取的IP地址为本地回环地址，则尝试获取本机配置的IP地址
                if (ipAddress.equals("127.0.0.1")) {
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，提取出第一个有效的IP地址作为用户真实IP地址
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            // 如果发生异常，则返回空字符串
            ipAddress = "";
        }
        return ipAddress;
    }


    /**
     * 获取全部请求头
     *
     * @param request HttpServletRequest对象，用于获取请求头信息
     * @return 返回一个Map对象，其中包含所有的请求头信息
     */
    public static Map<String, String> getAllRequestHeader(HttpServletRequest request) {
        // 获取请求头的名称集合
        Enumeration<String> headerNames = request.getHeaderNames();
        // 创建一个HashMap用于存储请求头的键值对
        Map<String, String> map = new HashMap<>();
        // 遍历请求头的名称集合
        while (headerNames.hasMoreElements()) {
            // 获取下一个请求头的名称
            String key = (String) headerNames.nextElement();
            // 根据名称获取请求头的值
            String value = request.getHeader(key);
            // 将请求头的键值对存入Map中
            map.put(key, value);
        }

        // 返回包含所有请求头信息的Map对象
        return map;
    }


    /**
     * MD5加密
     *
     * @param data 待加密的数据
     * @return 加密后的数据，如果加密过程中发生异常则返回null
     */
    public static String MD5(String data) {
        try {
            // 获取MD5加密器实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 对数据进行加密
            byte[] array = md.digest(data.getBytes("UTF-8"));
            // 创建字符串构建器用于拼接加密后的数据
            StringBuilder sb = new StringBuilder();
            // 遍历字节数组，将每个字节转换为十六进制字符串并拼接
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            // 返回转换为大写后的加密字符串
            return sb.toString().toUpperCase();
        } catch (Exception exception) {
            // 异常处理：如果发生异常则返回null
        }
        return null;
    }


    /**
     * 获取验证码随机数
     * <p>
     * 该方法用于生成指定长度的随机数字字符串，常用于生成验证码
     *
     * @param length 验证码的长度，即随机生成的数字字符串的长度
     * @return 生成的随机数字字符串
     */
    public static String getRandomCode(int length) {

        // 定义随机数源，这里只包含数字
        String sources = "0123456789";
        // 创建Random对象用于生成随机数
        Random random = new Random();
        // 使用StringBuilder来构建最终的随机数字符串
        StringBuilder sb = new StringBuilder();
        // 循环length次，每次从sources中随机取一个字符添加到sb中
        for (int j = 0; j < length; j++) {
            // random.nextInt(9)生成一个0到8之间的随机数，用于从sources中取字符
            sb.append(sources.charAt(random.nextInt(9)));
        }
        // 将StringBuilder对象转换为String并返回
        return sb.toString();
    }


    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳，单位为毫秒
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }


    /**
     * 生成uuid
     *
     * @return 生成的UUID字符串，无连字符，长度为32
     */
    public static String generateUUID() {
        // 使用UUID的randomUUID方法生成一个全局唯一标识符
        // 转换为字符串后移除所有连字符，然后截取前32个字符
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 获取随机长度的串
     *
     * @param length
     * @return
     */
    private static final String ALL_CHAR_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 生成指定长度的随机字符串，包含数字和字母
     *
     * @param length 随机字符串的长度
     * @return 生成的随机字符串
     */
    public static String getStringNumRandom(int length) {
        // 生成随机数字和字母,
        Random random = new Random();
        // 初始化StringBuilder，用于拼接生成的随机字符串
        StringBuilder saltString = new StringBuilder(length);
        // 循环指定次数，每次随机选取一个字符添加到StringBuilder中
        for (int i = 1; i <= length; ++i) {
            // 从ALL_CHAR_NUM中随机选取一个字符，并将其添加到saltString中
            saltString.append(ALL_CHAR_NUM.charAt(random.nextInt(ALL_CHAR_NUM.length())));
        }
        // 将生成的随机字符串转换为String类型并返回
        return saltString.toString();
    }


    /**
     * 响应json数据给前端
     *
     * @param response
     * @param obj
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) {

        response.setContentType("application/json; charset=utf-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.print(JsonUtil.obj2Json(obj));
            response.flushBuffer();

        } catch (IOException e) {
            logger.warn("响应json数据给前端异常:{}", e);
        }


    }

    /**
     * 计算字符串的MurmurHash32值
     * MurmurHash是一个非密码学的哈希函数，适用于一般的数据结构和算法中
     * 它以其良好的分布性、高性能和低碰撞率而著称
     *
     * @param param 需要计算哈希值的字符串
     * @return 计算得到的哈希值
     */
    public static long murmurHash32(String param) {
        // 使用Guava库中的Hashing类来计算MurmurHash32值
        long murmurHash32 = Hashing.murmur3_32().hashUnencodedChars(param).padToLong();
        return murmurHash32;
    }

}