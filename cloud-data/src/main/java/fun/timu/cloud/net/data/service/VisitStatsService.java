package fun.timu.cloud.net.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.data.controller.request.VisitRecordPageRequest;
import fun.timu.cloud.net.data.model.DO.VisitStats;

import java.util.Map;

/**
 * @author zhengke
 * @description 针对表【visit_stats】的数据库操作Service
 * @createDate 2025-05-02 22:15:26
 */
public interface VisitStatsService extends IService<VisitStats> {
    Map<String,Object> pageVisitRecord(VisitRecordPageRequest request);
}
