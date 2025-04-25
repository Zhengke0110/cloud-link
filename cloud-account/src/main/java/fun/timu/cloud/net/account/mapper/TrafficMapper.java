package fun.timu.cloud.net.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.timu.cloud.net.account.model.DO.Traffic;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhengke
 * @description 针对表【traffic】的数据库操作Mapper
 * @createDate 2025-04-12 09:08:16
 * @Entity generator.domain.Traffic
 */
public interface TrafficMapper extends BaseMapper<Traffic> {

    /**
     * 给某个流量包增加天使用次数
     *
     * @param accountNo
     * @param trafficId
     * @param usedTimes
     * @return
     */
    int addDayUsedTimes(@Param("accountNo") Long accountNo, @Param("trafficId") Long trafficId, @Param("usedTimes") Integer usedTimes);

    /**
     * 恢复某个流量包使用次数
     *
     * @param accountNo
     * @param trafficId
     * @param usedTimes
     * @return
     */
    int releaseUsedTimes(@Param("accountNo") Long accountNo, @Param("trafficId") Long trafficId, @Param("usedTimes") Integer usedTimes);

}




