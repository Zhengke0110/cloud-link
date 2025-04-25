package fun.timu.cloud.net.account.manager.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.timu.cloud.net.account.manager.AccountManager;
import fun.timu.cloud.net.account.manager.TrafficManager;
import fun.timu.cloud.net.account.mapper.TrafficMapper;
import fun.timu.cloud.net.account.model.DO.Traffic;
import fun.timu.cloud.net.common.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TrafficManagerImpl implements TrafficManager {
    private static Logger logger = LoggerFactory.getLogger(TrafficManager.class);
    private final TrafficMapper trafficMapper;

    public TrafficManagerImpl(TrafficMapper trafficMapper) {
        this.trafficMapper = trafficMapper;
    }

    /**
     * 添加交通数据
     * <p>
     * 该方法使用交通数据访问对象（TrafficMapper）将新的交通数据插入数据库
     * 它重写了父类或接口中的add方法，以实现具体的数据插入逻辑
     *
     * @param trafficDO 交通数据对象，包含要插入的交通数据信息
     * @return 插入操作影响的行数，通常为1表示成功，0表示失败
     */
    @Override
    public int add(Traffic trafficDO) {
        return trafficMapper.insert(trafficDO);
    }

    /**
     * 分页查询可用的流量记录
     *
     * @param page      页码，从1开始
     * @param size      每页大小
     * @param accountNo 账户编号
     * @return 返回分页的流量记录
     * <p>
     * 该方法用于查询未过期的流量记录，根据账户编号和过期日期进行筛选，
     * 并按照创建时间降序排列，以分页的形式返回查询结果
     */
    @Override
    public IPage<Traffic> pageAvailable(int page, int size, Long accountNo) {
        // 初始化分页对象
        Page<Traffic> pageInfo = new Page<>(page, size);
        // 获取今天的日期，格式为yyyy-MM-dd
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");

        // 调用Mapper方法进行分页查询，筛选条件为账户编号等于accountNo且过期日期大于等于今天，
        // 结果按创建时间降序排列
        Page<Traffic> trafficDOPage = trafficMapper.selectPage(pageInfo, new QueryWrapper<Traffic>()
                .eq("account_no", accountNo)
                .ge("expired_date", today)
                .orderByDesc("gmt_create"));

        // 返回查询结果
        return trafficDOPage;
    }

    /**
     * 根据流量ID和账户编号查找流量记录
     *
     * @param trafficId 流量记录的唯一标识符
     * @param accountNo 账户编号，用于关联特定的账户
     * @return 返回匹配的流量记录对象，如果没有找到匹配的记录，则返回null
     */
    @Override
    public Traffic findByIdAndAccountNo(Long trafficId, Long accountNo) {
        // 使用MyBatis-Plus的QueryWrapper构建查询条件
        // 查询条件为：账户编号等于accountNo且流量记录ID等于trafficId
        Traffic trafficDO = trafficMapper.selectOne(new QueryWrapper<Traffic>()
                .eq("account_no", accountNo)
                .eq("id", trafficId));
        return trafficDO;
    }

    /**
     * 删除过期的流量包
     * <p>
     * 本方法通过查询 Traffic 表中过期日期小于等于当前日期的记录，并将其删除
     * 主要用于清理数据库中不再有效的流量包信息，确保数据库的高效运行
     *
     * @return boolean 表示删除操作是否执行完成，此方法中直接返回 true，不反映实际删除结果
     */
    @Override
    public boolean deleteExpireTraffic() {
        // 执行删除操作，条件是过期日期小于等于当前日期
        int rows = trafficMapper.delete(new QueryWrapper<Traffic>()
                .le("expired_date", new Date()));
        // 记录删除操作的日志信息
        logger.info("删除过期流量包行数：rows={}", rows);
        // 直接返回 true，表示删除操作已执行
        return true;
    }

    /**
     * 根据账户编号选择可用的流量记录
     *
     * @param accountNo 账户编号，用于查询与账户关联的流量记录
     * @return 返回一个包含可用流量记录的列表
     */
    @Override
    public List<Traffic> selectAvailableTraffics(Long accountNo) {
        // 获取当前日期，格式为"yyyy-MM-dd"，用于比较流量记录的过期日期
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");

        // 创建一个QueryWrapper对象，用于构建查询条件
        QueryWrapper<Traffic> queryWrapper = new QueryWrapper<Traffic>();

        // 添加查询条件：账户编号等于参数accountNo，以筛选出属于指定账户的流量记录
        queryWrapper.eq("account_no", accountNo);
        // 添加查询条件：过期日期大于等于今天或者流量记录是初始免费流量
        // 这样可以确保只选取未过期或初始免费的流量记录
        queryWrapper.and(wrapper -> wrapper
                .ge("expired_date", today)
                .or().eq("out_trade_no", "free_init"));

        // 调用trafficMapper的selectList方法，传入构建好的查询条件，获取并返回可用的流量记录列表
        return trafficMapper.selectList(queryWrapper);
    }

    /**
     * 增加指定账户在一天内的使用次数
     * 此方法用于更新数据库中特定账户和流量ID的每日使用次数
     *
     * @param accountNo 账户编号，用于标识特定的账户
     * @param trafficId 流量ID，用于标识特定的流量记录
     * @param usedTimes 使用次数，将被添加到当前的使用次数中
     * @return 返回更新操作影响的行数，用于确认更新操作是否成功
     */
    @Override
    public int addDayUsedTimes(Long accountNo, Long trafficId, Integer usedTimes) {
        return trafficMapper.addDayUsedTimes(accountNo, trafficId, usedTimes);
    }

    /**
     * 释放已用流量次数
     * 当流量账户中的流量被归还时调用此方法，以更新数据库中的已用流量次数
     *
     * @param accountNo 流量账户编号，用于标识特定的流量账户
     * @param trafficId 流量ID，用于标识特定类型的流量
     * @param usedTimes 归还的流量次数，表示要从已用次数中减去的量
     * @return 返回更新影响的行数，用于确认更新操作是否成功
     */
    @Override
    public int releaseUsedTimes(Long accountNo, Long trafficId, Integer usedTimes) {
        return trafficMapper.releaseUsedTimes(accountNo, trafficId, usedTimes);
    }

    /**
     * 批量更新指定账户的流量使用次数
     * 此方法将指定账户下所有指定ID的流量记录的日使用次数重置为0
     *
     * @param accountNo           账户编号，用于标识哪个账户的流量记录需要更新
     * @param unUpdatedTrafficIds 未更新的流量记录ID列表，指定需要更新的流量记录
     * @return 返回更新操作影响的数据库行数，表示成功更新的流量记录数
     */
    @Override
    public int batchUpdateUsedTimes(Long accountNo, List<Long> unUpdatedTrafficIds) {
        // 执行批量更新操作，重置指定账户和流量记录的日使用次数为0
        int rows = trafficMapper.update(null, new UpdateWrapper<Traffic>()
                .eq("account_no", accountNo)
                .in("id", unUpdatedTrafficIds)
                .set("day_used", 0));

        // 返回更新操作影响的行数
        return rows;
    }
}
