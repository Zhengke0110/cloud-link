package fun.timu.cloud.net.shop.component;


import fun.timu.cloud.net.shop.model.VO.PayInfoVO;

/**
 * 支付策略上下文类，用于在运行时选择具体的支付策略并执行支付相关的操作
 */
public class PayStrategyContext {

    /**
     * 保存当前使用的支付策略
     */
    private PayStrategy payStrategy;

    /**
     * 构造方法，初始化支付策略
     *
     * @param payStrategy 具体的支付策略对象
     */
    public PayStrategyContext(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }

    /**
     * 根据策略对象，执行不同的下单接口
     *
     * @param payInfoVO 包含支付信息的值对象
     * @return 下单操作的结果字符串
     */
    public String executeUnifiedOrder(PayInfoVO payInfoVO) {

        return payStrategy.unifiedOrder(payInfoVO);
    }

    /**
     * 根据策略对象，执行不同的退款接口
     *
     * @param payInfoVO 包含支付信息的值对象
     * @return 退款操作的结果字符串
     */
    public String executeRefund(PayInfoVO payInfoVO) {

        return payStrategy.refund(payInfoVO);
    }

    /**
     * 根据策略对象，执行不同的关闭接口
     *
     * @param payInfoVO 包含支付信息的值对象
     * @return 关闭订单操作的结果字符串
     */
    public String executeCloseOrder(PayInfoVO payInfoVO) {

        return payStrategy.closeOrder(payInfoVO);
    }

    /**
     * 根据策略对象，执行不同的查询订单状态接口
     *
     * @param payInfoVO 包含支付信息的值对象
     * @return 查询支付状态操作的结果字符串
     */
    public String executeQueryPayStatus(PayInfoVO payInfoVO) {

        return payStrategy.queryPayStatus(payInfoVO);
    }

}

