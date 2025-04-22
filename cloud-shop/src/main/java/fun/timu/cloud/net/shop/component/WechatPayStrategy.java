package fun.timu.cloud.net.shop.component;

import fun.timu.cloud.net.shop.model.VO.PayInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WechatPayStrategy implements PayStrategy {
    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        String result = "注意:微信支付接口未实现, 这里将模拟成功返回微信的支付URL";
        return result;
    }

    @Override
    public String refund(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String queryPayStatus(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String closeOrder(PayInfoVO payInfoVO) {
        return null;
    }
}