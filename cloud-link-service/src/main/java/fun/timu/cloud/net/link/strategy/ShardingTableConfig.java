package fun.timu.cloud.net.link.strategy;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShardingTableConfig {

    /**
     * 存储数据表位置编号
     */
    private static final List<String> tableSuffixList = new ArrayList<>();

    private static Random random = new Random();

    // TODO 配置启用那些表的后缀
    static {
        tableSuffixList.add("0");
        tableSuffixList.add("a");
    }


    /**
     * 获取随机的后缀
     * @return 随机选择的后缀字符串
     */
    public static String getRandomTableSuffix(String code ){
        // 根据输入的code生成一个哈希值
        int hashCode = code.hashCode();
        // 使用取绝对值的方法确保哈希值为非负数
        // 使用取模运算根据哈希值选择一个后缀索引，以实现基本的负载均衡
        int index = Math.abs(hashCode) % tableSuffixList.size();
        // 返回根据索引选择的后缀
        return tableSuffixList.get(index);
    }


}
