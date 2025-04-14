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

    //配置启用那些表的后缀
    static {
        tableSuffixList.add("0");
        tableSuffixList.add("a");
    }


    /**
     * 获取随机的后缀
     * @return 随机选择的后缀字符串
     */
    public static String getRandomTableSuffix(){
        // 随机生成一个索引，用于从后缀列表中选择一个后缀
        int index = random.nextInt(tableSuffixList.size());
        // 根据生成的索引，从后缀列表中获取对应的后缀并返回
        return tableSuffixList.get(index);
    }



}
