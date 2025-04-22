package fun.timu.cloud.net.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import fun.timu.cloud.net.account.manager.AccountManager;
import fun.timu.cloud.net.account.manager.TrafficManager;
import fun.timu.cloud.net.account.mapper.TrafficMapper;
import fun.timu.cloud.net.account.model.DO.Traffic;
import fun.timu.cloud.net.account.model.VO.ProductVO;
import fun.timu.cloud.net.account.service.TrafficService;
import fun.timu.cloud.net.common.enums.EventMessageType;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * @author zhengke
 * @description 针对表【traffic】的数据库操作Service实现
 * @createDate 2025-04-22 15:18:25
 */
@Service
public class TrafficServiceImpl extends ServiceImpl<TrafficMapper, Traffic> implements TrafficService {
    private static Logger logger = LoggerFactory.getLogger(TrafficService.class);

    private final TrafficManager trafficManager;

    public TrafficServiceImpl(TrafficManager trafficManager) {
        this.trafficManager = trafficManager;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void handleTrafficMessage(EventMessage eventMessage) {
        // 获取消息类型
        String messageType = eventMessage.getEventMessageType();
        // 判断消息类型是否为产品订单支付
        if (EventMessageType.PRODUCT_ORDER_PAY.name().equalsIgnoreCase(messageType)) {

            //订单已经支付，新增流量

            // 获取消息内容
            String content = eventMessage.getContent();
            // 将消息内容转换为Map对象
            Map<String, Object> orderInfoMap = JsonUtil.json2Obj(content, Map.class);

            //还原订单商品信息
            Long accountNo = (Long) orderInfoMap.get("accountNo");
            String outTradeNo = (String) orderInfoMap.get("outTradeNo");
            Integer buyNum = (Integer) orderInfoMap.get("buyNum");
            String productStr = (String) orderInfoMap.get("product");
            ProductVO productVO = JsonUtil.json2Obj(productStr, ProductVO.class);
            logger.info("商品信息:{}", productVO);

            //计算流量包有效期
            LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(productVO.getValidDay());
            Date date = Date.from(expiredDateTime.atZone(ZoneId.systemDefault()).toInstant());

            //构建流量包对象
            Traffic trafficDO = Traffic.builder().accountNo(accountNo).dayLimit(productVO.getDayTimes() * buyNum).dayUsed(0).totalLimit(productVO.getTotalTimes()).pluginType(productVO.getPluginType()).level(productVO.getLevel()).productId(productVO.getId()).outTradeNo(outTradeNo).expiredDate(date).build();

            //添加流量包到数据库
            int rows = trafficManager.add(trafficDO);
            logger.info("消费消息新增流量包:rows={},trafficDO={}", rows, trafficDO);

        }
    }
}




