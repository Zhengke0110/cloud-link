package fun.timu.cloud.net.link.strategy;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShardingDBConfig {

    /**
     * 存储数据库位置编号
     */
    private static final List<String> dbPrefixList = new ArrayList<>();

    // 用于生成随机数
    private static Random random = new Random();

    // TODO 配置启用那些库的前缀
    static {
        dbPrefixList.add("0");
        dbPrefixList.add("1");
        dbPrefixList.add("a");
    }

    /**
     * 获取随机的前缀
     *
     * @return 随机选择的数据库前缀
     */
    public static String getRandomDBPrefix(String code) {
        // 根据输入的code生成哈希值
        int hashCode = code.hashCode();
        // 使用取模运算根据哈希值选择一个数据库前缀的索引
        int index = Math.abs(hashCode) % dbPrefixList.size();
        // 返回选中的数据库前缀
        return dbPrefixList.get(index);
    }

}

