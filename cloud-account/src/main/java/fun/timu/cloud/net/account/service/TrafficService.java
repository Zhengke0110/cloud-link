package fun.timu.cloud.net.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.account.model.DO.Traffic;
import fun.timu.cloud.net.common.model.EventMessage;

/**
 * @author zhengke
 * @description 针对表【traffic】的数据库操作Service
 * @createDate 2025-04-22 15:18:25
 */
public interface TrafficService extends IService<Traffic> {
    void handleTrafficMessage(EventMessage eventMessage);
}
