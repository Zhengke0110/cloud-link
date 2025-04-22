package fun.timu.cloud.net.shop.component;

import fun.timu.cloud.net.shop.model.VO.PayInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WechatPayStrategy implements PayStrategy {
    /**
     * 统一下单接口
     * 该方法旨在调用微信支付的统一下单接口，生成支付URL或订单信息
     * 当前实现是模拟成功返回微信的支付URL，实际的微信支付接口调用需根据实际情况进行开发
     *
     * @param payInfoVO 支付信息对象，包含下单所需的信息，如商品描述、订单号等
     * @return 返回模拟的微信支付URL字符串
     */
    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        log.info("微信支付统一下单接口");
        // 模拟成功返回微信的支付URL
        String result = "注意:微信支付接口未实现, 这里将模拟成功返回微信的支付URL";
        return result;
    }

    @Override
    public String refund(PayInfoVO payInfoVO) {
        return null;
    }

    /**
     * 查询支付状态
     * 此方法用于查询支付信息，根据当前的支付环境和参数，返回支付状态
     * 由于微信支付相关接口未实现，这里默认返回支付成功的信息
     *
     * @param payInfoVO 包含支付信息的参数对象，用于查询支付状态
     * @return 返回一个字符串，表示支付状态
     */
    @Override
    public String queryPayStatus(PayInfoVO payInfoVO) {
        // 记录查询支付结果的日志信息
        log.info("查询微信支付结果");
        // 由于微信支付相关接口未实现，这里默认返回查询支付成功的字符串信息
        String result = "注意:微信支付相关接口未实现, 这里默认查询支付成功";
        return result;
    }

    @Override
    public String closeOrder(PayInfoVO payInfoVO) {
        return null;
    }
}