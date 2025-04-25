package fun.timu.cloud.net.account.manager;


import com.baomidou.mybatisplus.core.metadata.IPage;
import fun.timu.cloud.net.account.model.DO.Traffic;

import java.util.List;

/**
 * TrafficManager接口定义了流量包管理的相关操作
 */
public interface TrafficManager {

    /**
     * 新增流量包
     *
     * @param trafficDO 流量包对象，包含流量包的详细信息
     * @return 新增流量包的数量，通常为1
     */
    int add(Traffic trafficDO);

    /**
     * 分页查询可用的流量包
     *
     * @param page      页码，从1开始
     * @param size      每页的流量包数量
     * @param accountNo 账户编号，用于筛选属于特定账户的流量包
     * @return 返回一个分页对象，包含可用的流量包列表
     */
    IPage<Traffic> pageAvailable(int page, int size, Long accountNo);

    /**
     * 查找详情
     *
     * @param trafficId 流量包ID，用于标识特定的流量包
     * @param accountNo 账户编号，用于筛选属于特定账户的流量包
     * @return 返回找到的流量包对象，如果找不到则返回null
     */
    Traffic findByIdAndAccountNo(Long trafficId, Long accountNo);

    /**
     * 删除过期流量包
     *
     * @return 如果删除成功则返回true，否则返回false
     */
    boolean deleteExpireTraffic();

    /**
     * 查找可用的短链流量包(未过期)，包括免费流量包
     *
     * @param accountNo 账户编号，用于筛选属于特定账户的流量包
     * @return 返回一个列表，包含所有可用的流量包
     */
    List<Traffic> selectAvailableTraffics(Long accountNo);

    /**
     * 添加流量包使用次数
     *
     * @param accountNo 账户编号，用于筛选属于特定账户的流量包
     * @param trafficId 流量包ID，用于标识特定的流量包
     * @param usedTimes 使用次数，表示流量包已被使用的次数
     * @return 更新的流量包数量，通常为1
     */
    int addDayUsedTimes(Long accountNo, Long trafficId, Integer usedTimes);

    /**
     * 恢复流量包使用当天次数
     *
     * @param accountNo 账户编号，用于筛选属于特定账户的流量包
     * @param trafficId 流量包ID，用于标识特定的流量包
     * @param useTimes  恢复的使用次数
     * @return 返回更新的流量包数量，通常为1
     */
    int releaseUsedTimes(Long accountNo, Long trafficId, Integer useTimes);

    /**
     * 批量更新流量包使用次数为0
     *
     * @param accountNo           账户编号，用于筛选属于特定账户的流量包
     * @param unUpdatedTrafficIds 未更新的流量包ID列表，用于标识特定的流量包
     * @return 返回更新的流量包数量
     */
    int batchUpdateUsedTimes(Long accountNo, List<Long> unUpdatedTrafficIds);

}


