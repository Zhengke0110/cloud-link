package fun.timu.cloud.net.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.shop.controller.request.ConfirmOrderRequest;
import fun.timu.cloud.net.shop.controller.request.ProductOrderPageRequest;
import fun.timu.cloud.net.shop.model.DO.ProductOrder;

import java.util.Map;

/**
 * @author zhengke
 * @description 针对表【product_order】的数据库操作Service
 * @createDate 2025-04-18 10:28:49
 */
public interface ProductOrderService extends IService<ProductOrder> {
    /**
     * 分页查询产品订单信息
     * 此方法接收一个ProductOrderPageRequest对象作为参数，该对象包含了分页和查询条件信息
     * 返回一个Map对象，其中包含了查询结果和分页相关信息
     *
     * @param orderPageRequest 包含分页和查询条件的请求对象
     * @return 包含查询结果和分页信息的Map对象
     */
    Map<String, Object> page(ProductOrderPageRequest orderPageRequest);

    /**
     * 查询产品订单的状态
     * 通过订单的外部交易编号来查询其状态，外部交易编号是系统外部生成的唯一标识符
     *
     * @param outTradeNo 外部交易编号
     * @return 订单状态的字符串表示
     */
    String queryProductOrderState(String outTradeNo);

    /**
     * 确认订单
     * 此方法接收一个ConfirmOrderRequest对象，其中包含了确认订单所需的信息
     * 返回一个JsonData对象，它包含了操作结果信息
     *
     * @param orderRequest 包含确认订单信息的请求对象
     * @return 包含操作结果信息的JsonData对象
     */
    JsonData confirmOrder(ConfirmOrderRequest orderRequest);
}
