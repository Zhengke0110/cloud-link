package fun.timu.cloud.net.shop.controller;

import fun.timu.cloud.net.common.constant.RedisKey;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.enums.ClientTypeEnum;
import fun.timu.cloud.net.common.enums.ProductOrderPayTypeEnum;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.shop.annotation.RepeatSubmit;
import fun.timu.cloud.net.shop.controller.request.ConfirmOrderRequest;
import fun.timu.cloud.net.shop.controller.request.ProductOrderPageRequest;
import fun.timu.cloud.net.shop.service.ProductOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/order/v1")
public class ProductOrderController {
    private static Logger logger = LoggerFactory.getLogger(ProductOrderController.class);

    private final ProductOrderService productOrderService;
    private final StringRedisTemplate redisTemplate;

    public ProductOrderController(ProductOrderService productOrderService, StringRedisTemplate redisTemplate) {
        this.productOrderService = productOrderService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取下单令牌
     * <p>
     * 本方法用于生成和返回一个下单所需的令牌(token)，以确保用户在下单时的操作合法性
     * 它首先从ThreadLocal中获取当前用户的账户编号，然后生成一个随机令牌，并将该令牌与账户编号组合，
     * 作为Redis中的键，用于存储和验证令牌的有效性
     *
     * @return 返回包含生成的令牌的JsonData对象如果生成或存储令牌失败，将返回相应的错误信息
     */
    @GetMapping("token")
    public JsonData getOrderToken() {
        // 从ThreadLocal中获取当前登录用户的账户编号
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 生成一个32位的随机令牌
        String token = CommonUtil.getStringNumRandom(32);

        // 构造Redis中的键，用于存储令牌
        String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY, accountNo, token);

        // 将令牌存储到Redis中，令牌有效时间是30分钟
        redisTemplate.opsForValue().set(key, String.valueOf(Thread.currentThread().getId()), 30, TimeUnit.MINUTES);

        // 返回生成的令牌
        return JsonData.buildSuccess(token);
    }

    /**
     * 处理产品订单的分页查询请求
     * <p>
     * 该方法接收一个ProductOrderPageRequest对象作为参数，其中包含了分页查询的相关信息，
     * 如页码、每页记录数以及可能的筛选条件等它通过HTTP POST方法接收请求，
     * 请求体中包含分页查询的具体参数，并返回一个JsonData对象，其中包含了查询结果
     *
     * @param orderPageRequest 包含分页查询信息的请求对象
     * @return 包含查询结果的JsonData对象
     */
    @PostMapping("page")
    public JsonData page(@RequestBody ProductOrderPageRequest orderPageRequest) {

        // 调用服务层的分页查询方法，获取查询结果
        Map<String, Object> pageResult = productOrderService.page(orderPageRequest);

        // 构建并返回成功的JsonData对象，其中包含查询结果
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
