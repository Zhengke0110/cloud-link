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

@Component
public class TrafficManagerImpl implements TrafficManager {
    private static Logger logger = LoggerFactory.getLogger(AccountManager.class);
    private final TrafficMapper trafficMapper;

    public TrafficManagerImpl(TrafficMapper trafficMapper) {
        this.trafficMapper = trafficMapper;
    }

    /**
     * 添加交通数据
     *
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
     * @param page 页码，从1开始
     * @param size 每页大小
     * @param accountNo 账户编号
     * @return 返回分页的流量记录
     *
     * 该方法用于查询未过期的流量记录，根据账户编号和过期日期进行筛选，并按照创建时间降序排列
     * 使用分页查询以提高查询效率，避免一次性加载大量数据
     */
    @Override
    public IPage<Traffic> pageAvailable(int page, int size, Long accountNo) {
        // 初始化分页对象
        Page<Traffic> pageInfo = new Page<>(page, size);
        // 获取今天的日期，格式为yyyy-MM-dd
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");

        // 调用Mapper方法进行分页查询，筛选条件为账户编号等于accountNo且过期日期大于等于今天，结果按创建时间降序排列
        Page<Traffic> trafficDOPage = trafficMapper.selectPage(pageInfo, new QueryWrapper<Traffic>()
                .eq("account_no", accountNo).ge("expired_date", today).orderByDesc("gmt_create"));

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
                .eq("account_no", accountNo).eq("id", trafficId));
        return trafficDO;
    }

    /**
     * 给某个流量包增加天使用次数
     * 该方法通过更新数据库中的流量包记录来增加其当天的使用次数
     * 主要用于跟踪流量包的每日使用情况，以便进行流量管理或统计分析
     *
     * @param currentTrafficId 流量包的唯一标识符，用于指定需要更新的流量包
     * @param accountNo 账户编号，用于确定流量包所属的账户
     * @param dayUsedTimes 当天已使用的次数，这个值将被更新到数据库中
     * @return 返回更新操作影响的行数，通常为1表示成功更新，0表示未找到对应的流量包记录
     */
    @Override
    public int addDayUsedTimes(long currentTrafficId, Long accountNo, int dayUsedTimes) {
        // 更新指定账户和流量包ID的当天使用次数
        return trafficMapper.update(null, new UpdateWrapper<Traffic>()
                .eq("account_no", accountNo)
                .eq("id", currentTrafficId).set("day_used", dayUsedTimes));
    }
}
