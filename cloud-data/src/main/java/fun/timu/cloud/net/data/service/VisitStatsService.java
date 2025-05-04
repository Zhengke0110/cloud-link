package fun.timu.cloud.net.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.data.controller.request.*;
import fun.timu.cloud.net.data.model.DO.VisitStats;
import fun.timu.cloud.net.data.model.VO.VisitStatsVO;

import java.util.List;
import java.util.Map;

/**
 * @author zhengke
 * @description 针对表【visit_stats】的数据库操作Service
 * @createDate 2025-05-02 22:15:26
 */
public interface VisitStatsService extends IService<VisitStats> {
    /**
     * 分页查询访问记录
     *
     * @param request 分页查询请求对象，包含页码、页大小等信息
     * @return 返回一个Map对象，包含分页查询的结果，如总记录数、数据列表等
     */
    Map<String, Object> pageVisitRecord(VisitRecordPageRequest request);

    /**
     * 查询指定区域内的日访问统计
     *
     * @param request 查询请求对象，包含查询条件如区域信息、日期等
     * @return 返回一个列表，包含按区域和日期统计的访问信息
     */
    List<VisitStatsVO> queryRegionWithDay(RegionQueryRequest request);

    /**
     * 查询访问趋势
     *
     * @param request 查询请求对象，包含查询条件如时间范围等
     * @return 返回一个列表，包含按时间统计的访问趋势信息
     */
    List<VisitStatsVO> queryVisitTrend(VisitTrendQueryRequest request);

    /**
     * 查询频繁访问来源
     *
     * @param request 查询请求对象，包含查询条件如时间范围等
     * @return 返回一个列表，包含频繁访问的来源信息
     */
    List<VisitStatsVO> queryFrequentSource(FrequentSourceRequset request);

    /**
     * 查询设备信息统计
     *
     * @param request 查询请求对象，包含查询条件如时间范围等
     * @return 返回一个Map对象，键为设备类型，值为该类型的访问统计信息列表
     */
    Map<String, List<VisitStatsVO>> queryDeviceInfo(QueryDeviceRequest request);
}
