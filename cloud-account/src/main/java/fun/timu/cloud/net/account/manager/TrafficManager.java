package fun.timu.cloud.net.account.manager;


import com.baomidou.mybatisplus.core.metadata.IPage;
import fun.timu.cloud.net.account.model.DO.Traffic;

public interface TrafficManager {

    /**
     * 新增流量包
     *
     * @param trafficDO
     * @return
     */
    int add(Traffic trafficDO);


    /**
     * 分页查询可用的流量包
     *
     * @param page
     * @param size
     * @param accountNo
     * @return
     */
    IPage<Traffic> pageAvailable(int page, int size, Long accountNo);


    /**
     * 查找详情
     *
     * @param trafficId
     * @param accountNo
     * @return
     */
    Traffic findByIdAndAccountNo(Long trafficId, Long accountNo);


    /**
     * 增加某个流量包天使用次数
     *
     * @param currentTrafficId
     * @param accountNo
     * @param dayUsedTimes
     * @return
     */
    int addDayUsedTimes(long currentTrafficId, Long accountNo, int dayUsedTimes);


}
