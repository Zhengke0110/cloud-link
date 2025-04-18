package fun.timu.cloud.net.shop.controller;

import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.enums.ClientTypeEnum;
import fun.timu.cloud.net.common.enums.ProductOrderPayTypeEnum;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.shop.controller.request.ConfirmOrderRequest;
import fun.timu.cloud.net.shop.service.ProductOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/order/v1")
public class ProductOrderController {
    private static Logger logger = LoggerFactory.getLogger(ProductOrderController.class);

    private final ProductOrderService productOrderService;

    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    /**
     * 处理产品订单的分页查询请求
     * <p>
     * 此方法通过接收页码、页面大小和可选的状态参数来查询产品订单信息
     * 它利用Spring MVC的@RequestParam注解来获取请求参数，并提供默认值和可选参数的支持
     *
     * @param page  页码，用于指定当前查询的页数，默认值为1
     * @param size  页面大小，用于指定每页显示的记录数，默认值为10
     * @param state 订单状态，用于筛选特定状态的订单，是一个可选参数
     * @return 返回一个JsonData对象，其中包含分页查询的结果
     */
    @GetMapping("page")
    public JsonData page(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size, @RequestParam(value = "state", required = false) String state) {
        // 调用服务层的分页查询方法，并将结果转换为JsonData对象返回
        Map<String, Object> pageResult = productOrderService.page(page, size, state);
        return JsonData.buildSuccess(pageResult);
    }


    /**
     * 查询订单状态
     * <p>
     * 此方法通过订单号查询订单的状态，并以Json格式返回结果
     * 如果订单号不存在或状态为空，则返回特定的错误码
     * 否则，返回订单的当前状态
     *
     * @param outTradeNo 订单号，用于查询订单状态
     * @return 订单状态的Json数据，或者错误信息
     */
    @GetMapping("queryState")
    public JsonData queryState(@RequestParam(value = "outTradeNo") String outTradeNo) {

        // 调用服务层方法查询订单状态
        String state = productOrderService.queryProductOrderState(outTradeNo);

        // 根据查询结果构建并返回Json数据
        // 如果状态为空，则返回订单不存在的错误码
        // 否则，返回订单的状态信息
        return StringUtils.isBlank(state) ? JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST) : JsonData.buildSuccess(state);

    }


    /**
     * 确认订单接口
     * 根据订单请求信息，确认订单并根据支付类型和客户端类型进行相应处理
     *
     * @param orderRequest 订单请求对象，包含订单相关信息
     * @param response     用于向客户端发送响应
     */
    @PostMapping("confirm")
    public void confirmOrder(@RequestBody ConfirmOrderRequest orderRequest, HttpServletResponse response) {
        // 调用服务层确认订单方法，返回结果以jsonData封装
        JsonData jsonData = productOrderService.confirmOrder(orderRequest);

        // 如果jsonData的code为0，表示订单确认成功
        if (jsonData.getCode() == 0) {

            // 获取客户端类型
            String client = orderRequest.getClientType();
            // 获取支付类型
            String payType = orderRequest.getPayType();

            // 如果是支付宝支付，根据客户端类型进行处理
            if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.ALI_PAY.name())) {

                // 如果是PC端，发送HTML消息
                if (client.equalsIgnoreCase(ClientTypeEnum.PC.name())) {
                    CommonUtil.sendHtmlMessage(response, jsonData);
                }
                // 如果是APP端或H5端，暂无处理逻辑
                else if (client.equalsIgnoreCase(ClientTypeEnum.APP.name())) {
                } else if (client.equalsIgnoreCase(ClientTypeEnum.H5.name())) {
                }

            }
            // 如果是微信支付，发送JSON消息
            else if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT_APY.name())) {
                CommonUtil.sendJsonMessage(response, jsonData);
            }

        }
        // 如果订单确认失败，记录错误日志并发送JSON消息
        else {
            logger.error("创建订单失败{}", jsonData.toString());
            CommonUtil.sendJsonMessage(response, jsonData);
        }
    }

}
