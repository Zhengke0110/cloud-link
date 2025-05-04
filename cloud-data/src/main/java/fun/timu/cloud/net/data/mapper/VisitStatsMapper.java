package fun.timu.cloud.net.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.timu.cloud.net.data.model.DO.VisitStats;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【visit_stats】的数据库操作Mapper
 * @createDate 2025-05-02 22:15:26
 * @Entity generator.domain.VisitStats
 */
public interface VisitStatsMapper extends BaseMapper<VisitStats> {
    /**
     * 分页查询访问记录
     *
     * @param code      查询代码
     * @param accountNo 账户编号
     * @param from      起始位置
     * @param size      查询条数
     * @return 访问记录列表
     */
    List<VisitStats> pageVisitRecord(@Param("code") String code, @Param("accountNo") Long accountNo,
                                     @Param("from") int from, @Param("size") int size);

    /**
     * 计算总条数
     *
     * @param code      查询代码
     * @param accountNo 账户编号
     * @return 总条数
     */
    int countTotal(@Param("code") String code, @Param("accountNo") Long accountNo);

    /**
     * 根据时间范围查询地区访问分布
     *
     * @param code      查询代码
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param accountNo 账户编号
     * @return 地区访问分布列表
     */
    List<VisitStats> queryRegionVisitStatsWithDay(@Param("code") String code,
                                                  @Param("accountNo") Long accountNo,
                                                  @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime);

    /**
     * 查询时间范围内的访问趋势图 天级别
     *
     * @param code      查询代码
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param accountNo 账户编号
     * @return 访问趋势图列表（天级别）
     */
    List<VisitStats> queryVisitTrendWithMultiDay(@Param("code") String code, @Param("accountNo") Long accountNo, @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime);

    /**
     * 查询时间范围内的访问趋势图 小时级别
     *
     * @param code      查询代码
     * @param accountNo 账户编号
     * @param startTime 开始时间
     * @return 访问趋势图列表（小时级别）
     */
    List<VisitStats> queryVisitTrendWithHour(@Param("code") String code, @Param("accountNo") Long accountNo, @Param("startTime") String startTime);

    /**
     * 查询时间范围内的访问趋势图 分钟级别
     *
     * @param code      查询代码
     * @param accountNo 账户编号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 访问趋势图列表（分钟级别）
     */
    List<VisitStats> queryVisitTrendWithMinute(@Param("code") String code, @Param("accountNo") Long accountNo, @Param("startTime") String startTime,
                                               @Param("endTime") String endTime);

    /**
     * 查询高频访问来源
     *
     * @param code      查询代码
     * @param accountNo 账户编号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      查询条数
     * @return 高频访问来源列表
     */
    List<VisitStats> queryFrequentSource(@Param("code") String code, @Param("accountNo") Long accountNo, @Param("startTime") String startTime,
                                         @Param("endTime") String endTime, @Param("size") int size);

    /**
     * 查询设备类型
     *
     * @param code      查询代码
     * @param accountNo 账户编号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param field     查询字段
     * @return 设备类型列表
     */
    List<VisitStats> queryDeviceInfo(@Param("code") String code, @Param("accountNo") Long accountNo,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime, @Param("field") String field);
}



