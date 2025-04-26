package fun.timu.cloud.net.account.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.timu.cloud.net.account.manager.TrafficTaskManager;
import fun.timu.cloud.net.account.mapper.TrafficTaskMapper;
import fun.timu.cloud.net.account.model.DO.TrafficTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrafficTaskManagerImpl implements TrafficTaskManager {
    private static Logger logger = LoggerFactory.getLogger(TrafficTaskManager.class);
    private final TrafficTaskMapper trafficTaskMapper;

    public TrafficTaskManagerImpl(TrafficTaskMapper trafficTaskMapper) {
        this.trafficTaskMapper = trafficTaskMapper;
    }

    /**
     * 添加新的流量任务
     * <p>
     * 此方法通过调用trafficTaskMapper的insert方法来插入一个新的流量任务对象到数据库中
     * 它封装了底层的数据访问逻辑，为上层提供了一个简洁的接口
     *
     * @param trafficTaskDO 要添加的流量任务对象，包含流量任务的相关信息
     * @return 插入操作的结果，通常是一个表示受影响行数的整数
     */
    @Override
    public int add(TrafficTask trafficTaskDO) {
        return trafficTaskMapper.insert(trafficTaskDO);
    }

    /**
     * 根据ID和账户编号查找交通任务
     *
     * @param id        交通任务的唯一标识符
     * @param accountNo 账户编号，用于关联特定的账户
     * @return 返回找到的交通任务对象，如果没有找到则返回null
     */
    @Override
    public TrafficTask findByIdAndAccountNo(Long id, Long accountNo) {
        // 使用MyBatis-Plus的QueryWrapper构建查询条件
        // 查询条件为：根据id和account_no字段进行精确匹配
        TrafficTask taskDO = trafficTaskMapper.selectOne(new QueryWrapper<TrafficTask>().eq("id", id).eq("account_no", accountNo));
        return taskDO;
    }

    /**
     * 根据ID和账户编号删除交通任务记录
     *
     * @param id        交通任务的唯一标识符
     * @param accountNo 账户编号，用于确定交通任务所属的账户
     * @return 返回删除操作影响的行数，通常为1，如果未找到匹配的记录则为0
     */
    @Override
    public int deleteByIdAndAccountNo(Long id, Long accountNo) {
        // 构建查询条件，根据ID和账户编号查询交通任务记录
        QueryWrapper<TrafficTask> queryWrapper = new QueryWrapper<TrafficTask>().eq("id", id).eq("account_no", accountNo);

        // 执行删除操作，返回删除的记录数
        return trafficTaskMapper.delete(queryWrapper);
    }
}
