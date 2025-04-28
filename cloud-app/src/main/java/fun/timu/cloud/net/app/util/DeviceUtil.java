package fun.timu.cloud.net.app.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import fun.timu.cloud.net.app.model.DO.DeviceInfo;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.Map;


public class DeviceUtil {

    /**
     * 生成web设备唯一ID
     * <p>
     * 该方法通过将设备信息映射转换为字符串并使用MD5加密来生成唯一的设备ID
     * 使用MD5加密确保了即使设备信息映射发生变化，也能保持ID的唯一性和一致性
     *
     * @param map 包含设备信息的映射，如浏览器信息、操作系统等
     * @return 生成的唯一设备ID
     */
    public static String geneWebUniqueDeviceId(Map<String, String> map) {
        String deviceId = MD5(map.toString());
        return deviceId;
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
     * 获取浏览器对象
     * <p>
     * 该方法用于解析HTTP请求的User-Agent头部信息，并提取浏览器信息
     * User-Agent是一个包含客户端浏览器类型、版本、操作系统等信息的字符串
     * 通过解析这个字符串，我们可以获取到用户正在使用的浏览器的详细信息
     *
     * @param agent 用户代理字符串，包含浏览器信息
     * @return 返回一个Browser对象，代表解析后的浏览器信息
     */
    public static Browser getBrowser(String agent) {
        // 使用UserAgent解析工具解析用户代理字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        // 提取并返回浏览器信息
        return userAgent.getBrowser();
    }


    /**
     * 获取操作系统
     *
     * @param agent 用户代理字符串，包含了用户操作系统的信息
     * @return 返回用户的操作系统对象
     */
    public static OperatingSystem getOperationSystem(String agent) {
        // 解析用户代理字符串，获取用户操作系统信息
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        // 返回用户操作系统对象
        return userAgent.getOperatingSystem();
    }


    /**
     * 获取浏览器名称
     * <p>
     * 通过解析用户代理字符串(agent)，判断客户端使用的浏览器类型，并返回其名称
     * 目前仅支持识别Firefox和Chrome两种浏览器
     *
     * @param agent 用户代理字符串，包含了客户端浏览器的信息
     * @return 返回识别到的浏览器名称，可能的值为 "Firefox" 或 "Chrome"
     * 如果无法识别，将返回空字符串或默认值（具体取决于getBrowser方法的实现）
     */
    public static String getBrowserName(String agent) {
        // 调用getBrowser方法获取浏览器对象，并进一步调用getGroup().getName()获取浏览器名称
        // 这里假设getBrowser方法能够根据用户代理字符串解析并返回一个浏览器对象
        // 同时假设getGroup()方法返回一个包含浏览器分组信息的对象，其getName()方法返回分组名称
        return getBrowser(agent).getGroup().getName();
    }


    /**
     * 获取设备类型
     * <p>
     * 根据用户代理字符串判断设备类型
     * 该方法主要用于识别请求是来自移动设备还是计算机
     * 通过解析用户代理字符串中的操作系统信息来判断设备类型
     *
     * @param agent 用户代理字符串，通常来自HTTP请求的Header
     * @return 设备类型，可能的值为MOBILE（移动设备）或COMPUTER（计算机）
     */
    public static String getDeviceType(String agent) {
        return getOperationSystem(agent).getDeviceType().toString();
    }


    /**
     * 获取操作系统类型：Windows、iOS、Android
     *
     * @param agent 用户代理字符串，用于识别客户端操作系统
     * @return 操作系统名称
     */
    public static String getOS(String agent) {
        // 从用户代理字符串中解析操作系统信息，并返回操作系统名称
        return getOperationSystem(agent).getGroup().getName();
    }


    /**
     * 获取设备厂家
     * <p>
     * 通过解析用户代理字符串(agent)，提取并返回设备的制造商信息
     * 此方法依赖于getOperationSystem方法来解析操作系统信息，然后进一步获取制造商信息
     *
     * @param agent 用户代理字符串，包含了客户端的设备和浏览器信息
     * @return 设备制造商的字符串表示，如果无法解析或解析失败，则返回空字符串
     */
    public static String getDeviceManufacturer(String agent) {
        return getOperationSystem(agent).getManufacturer().toString();
    }


    /**
     * 获取操作系统版本
     *
     * @param userAgent 用户代理字符串，包含了客户端操作系统的信息
     * @return 返回操作系统的版本，如 Android 1.x、Intel Mac OS X 10.15 如果无法解析或userAgent为空，则返回空字符串
     */
    public static String getOSVersion(String userAgent) {
        // 初始化操作系统版本为空字符串
        String osVersion = "";
        // 检查userAgent是否为空或仅包含空白字符，如果为真，则直接返回空字符串
        if (StringUtils.isBlank(userAgent)) {
            return osVersion;
        }
        // 从userAgent中提取操作系统版本信息
        String[] strArr = userAgent.substring(userAgent.indexOf("(") + 1, userAgent.indexOf(")")).split(";");
        // 检查提取的结果是否为空或长度为0，如果为真，则直接返回空字符串
        if (null == strArr || strArr.length == 0) {
            return osVersion;
        }

        // 将操作系统版本信息赋值给osVersion变量
        osVersion = strArr[1];
        // 返回操作系统版本信息
        return osVersion;
    }


    /**
     * 解析对象
     *
     * @param agent 用户代理字符串
     * @return DeviceInfo对象，包含设备信息
     */
    public static DeviceInfo getDeviceInfo(String agent) {
        // 解析用户代理字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        // 获取浏览器信息
        Browser browser = userAgent.getBrowser();
        // 获取操作系统信息
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        // 获取浏览器名称
        String browserName = browser.getGroup().getName();
        // 获取操作系统名称
        String os = operatingSystem.getGroup().getName();
        // 获取设备制造商
        String manufacture = operatingSystem.getManufacturer().toString();
        // 获取设备类型
        String deviceType = operatingSystem.getDeviceType().toString();

        // 构建DeviceInfo对象
        DeviceInfo deviceInfoDO = DeviceInfo.builder().browserName(browserName).deviceManufacturer(manufacture).deviceType(deviceType).os(os).osVersion(getOSVersion(agent)) // 获取操作系统版本
                .build();

        // 返回DeviceInfo对象
        return deviceInfoDO;
    }

}
