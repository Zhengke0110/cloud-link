package fun.timu.cloud.net.link.strategy;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class CustomTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     * 根据分片属性和可用的数据源集合来确定具体的分片表
     * 该方法主要用于分库分表的场景下，根据特定的分片规则来选择实际操作的数据库表
     *
     * @param availableTargetNames 数据源集合
     *                             在分库时值为所有分片库的集合 databaseNames
     *                             分表时为对应分片库中所有分片表的集合 tablesNames
     * @param shardingValue        分片属性，包括
     *                             logicTableName 为逻辑表，
     *                             columnName 分片健（字段），
     *                             value 为从 SQL 中解析出的分片健的值
     * @return 返回具体的分片表名称
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {

        //获取逻辑表
        String targetName = availableTargetNames.iterator().next();

        //短链码  A23Ad1
        String value = shardingValue.getValue();


        //获取短链码最后一位
        String codeSuffix =  value.substring(value.length()-1);

        //拼接Actual table
        //根据获取到的短链码最后一位和逻辑表名拼接出实际操作的表名
        return targetName+"_"+codeSuffix;
    }
}
