package fun.timu.cloud.net.app.util;

import java.security.MessageDigest;
import java.util.Map;

public class DeviceUtil {

    /**
     * 生成web设备唯一ID
     *
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


}
