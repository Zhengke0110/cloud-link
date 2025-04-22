package fun.timu.cloud.net.shop.component;

import fun.timu.cloud.net.common.enums.ProductOrderPayTypeEnum;
import fun.timu.cloud.net.shop.model.VO.PayInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayFactory {
    @Autowired
    private AliPayStrategy aliPayStrategy;

    @Autowired
    private WechatPayStrategy wechatPayStrategy;

    /**
     * 执行支付操作
     * 根据提供的支付信息，选择相应的支付策略并执行统一订单处理
     *
     * @param payInfoVO 包含支付相关信息的对象
     * @return 支付结果或空字符串
     */
    public String pay(PayInfoVO payInfoVO){
        // 获取支付类型
        String payType = payInfoVO.getPayType();

        // 判断支付类型并执行相应的支付策略
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)) {
            // 支付宝支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(aliPayStrategy);
            return payStrategyContext.executeUnifiedOrder(payInfoVO);

        } else if(ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            // 微信支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeUnifiedOrder(payInfoVO);
        }
        // 如果支付类型不匹配，则返回空字符串
        return "";
    }

    /**
     * 关闭订单
     * 根据支付类型关闭相应的订单，支持支付宝和微信支付的订单关闭
     * @param payInfoVO 包含支付信息的对象，包括支付类型等信息
     * @return 关闭订单的结果，通常是一个表示成功或失败的字符串信息
     */
    public String closeOrder(PayInfoVO payInfoVO){

        // 获取支付类型
        String payType = payInfoVO.getPayType();

        // 根据支付类型执行相应的关闭订单操作
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)) {
            // 支付宝支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(aliPayStrategy);
            return payStrategyContext.executeCloseOrder(payInfoVO);

        } else if(ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){

            // 微信支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeCloseOrder(payInfoVO);
        }
        // 如果支付类型不匹配，则返回空字符串
        return "";
    }

    /**
     * 退款接口
     * @param payInfoVO
     * @return
     */
    public String refund(PayInfoVO payInfoVO){

        String payType = payInfoVO.getPayType();

        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)) {
            //支付宝支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(aliPayStrategy);
            return payStrategyContext.executeRefund(payInfoVO);

        } else if(ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            //微信支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeRefund(payInfoVO);
        }

        return "";
    }


}
