package fun.timu.cloud.net.link.component;

import fun.timu.cloud.net.util.CommonUtil;
import org.springframework.stereotype.Component;

@Component
public class ShortLinkComponent {

    /**
     * 62个字符
     */
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    /**
     * 生成短链码
     * <p>
     * 该方法用于将输入参数转换为短链码，主要步骤包括：
     * 1. 使用MurmurHash32算法对参数进行哈希计算，得到一个32位的哈希值
     * 2. 将得到的哈希值转换为62进制的字符串，作为短链码
     *
     * @param param 输入参数，可以是任何字符串，用于生成唯一的短链码
     * @return 生成的短链码，为一个字符串
     */
    public String createShortLinkCode(String param) {

        // 使用MurmurHash32算法对参数进行哈希计算
        long murmurhash = CommonUtil.murmurHash32(param);
        // 进制转换：将哈希值转换为62进制的字符串
        String code = encodeToBase62(murmurhash);

        return code;
    }

    /**
     * 10进制转62进制
     *
     * @param num 需要转换的10进制数字
     * @return 转换后的62进制字符串
     */
    private String encodeToBase62(long num) {

        // StringBuffer线程安全，StringBuilder线程不安全
        StringBuffer sb = new StringBuffer();
        do {
            // 取余数并查找对应的62进制字符
            int i = (int) (num % 62);
            sb.append(CHARS.charAt(i));
            // 更新num为除以62的商，继续下一轮循环
            num = num / 62;
        } while (num > 0);

        // 将字符序列反转以获得正确的62进制表示
        String value = sb.reverse().toString();
        return value;
    }
}
