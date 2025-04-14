package fun.timu.cloud.net.link.strategy;

import fun.timu.cloud.net.common.exception.BizException;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class CustomDBPreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     * 根据分片属性执行分片逻辑的方法
     * 该方法用于在给定的数据源集合中选择一个具体的分片（数据库或表）
     * 主要用于分库分表的场景，根据特定的分片规则来路由到正确的数据源
     *
     * @param availableTargetNames 数据源集合
     *                             在分库时值为所有分片库的集合 databaseNames
     *                             分表时为对应分片库中所有分片表的集合 tablesNames
     * @param shardingValue        分片属性，包括
     *                             logicTableName 为逻辑表，
     *                             columnName 分片健（字段），
     *                             value 为从 SQL 中解析出的分片健的值
     * @return 返回选中的数据源名称
     * @throws BizException 如果没有找到匹配的数据源，则抛出业务异常
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {

        //获取短链码第一位，即库位
        String codePrefix = shardingValue.getValue().substring(0, 1);

        for (String targetName : availableTargetNames) {
            //获取库名的最后一位，真实配置的ds
            String targetNameSuffix = targetName.substring(targetName.length() - 1);

            //如果一致则返回
            if (codePrefix.equals(targetNameSuffix)) {
                return targetName;
            }
        }

        //抛异常
        throw new BizException(BizCodeEnum.DB_ROUTE_NOT_FOUND);
    }
}
