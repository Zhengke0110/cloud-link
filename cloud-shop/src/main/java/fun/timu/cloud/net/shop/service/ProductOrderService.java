package fun.timu.cloud.net.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.shop.controller.request.ConfirmOrderRequest;
import fun.timu.cloud.net.shop.model.DO.ProductOrder;

import java.util.Map;

/**
 * @author zhengke
 * @description 针对表【product_order】的数据库操作Service
 * @createDate 2025-04-18 10:28:49
 */
public interface ProductOrderService extends IService<ProductOrder> {
    Map<String, Object> page(int page, int size, String state);

    String queryProductOrderState(String outTradeNo);

    JsonData confirmOrder(ConfirmOrderRequest orderRequest);
}
