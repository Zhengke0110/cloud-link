package fun.timu.cloud.net.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.account.controller.request.TrafficPageRequest;
import fun.timu.cloud.net.account.controller.request.UseTrafficRequest;
import fun.timu.cloud.net.account.model.DO.Traffic;
import fun.timu.cloud.net.account.model.VO.TrafficVO;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.util.JsonData;

import java.util.Map;

/**
 * @author zhengke
 * @description 针对表【traffic】的数据库操作Service
 * @createDate 2025-04-22 15:18:25
 */
public interface TrafficService extends IService<Traffic> {

    /**
     * 处理交通事件消息
     *
     * @param eventMessage 交通事件消息，包含交通事件的详细信息
     */
    void handleTrafficMessage(EventMessage eventMessage);

    /**
     * 分页查询可用的交通信息
     *
     * @param request 包含分页请求参数的对象
     * @return 返回一个映射，其中包含分页查询的结果
     */
    Map<String, Object> pageAvailable(TrafficPageRequest request);

    /**
     * 获取指定交通信息的详细视图
     *
     * @param trafficId 交通信息的唯一标识符
     * @return 返回一个包含交通信息详细数据的TrafficVO对象
     */
    TrafficVO detail(long trafficId);

    /**
     * 删除过期流量包
     *
     * @return 如果删除成功，则返回true；否则返回false
     */
    boolean deleteExpireTraffic();

    /**
     * 扣减流量包
     *
     * @param useTrafficRequest 包含扣减流量请求参数的对象
     * @return 返回一个JsonData对象，其中包含扣减操作的结果
     */
    JsonData reduce(UseTrafficRequest useTrafficRequest);

}
