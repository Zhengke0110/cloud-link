package fun.timu.cloud.net.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import fun.timu.cloud.net.common.constant.TimeConstant;
import fun.timu.cloud.net.common.enums.*;
import fun.timu.cloud.net.common.exception.BizException;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.model.LoginUser;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.common.util.JsonUtil;
import fun.timu.cloud.net.shop.component.PayFactory;
import fun.timu.cloud.net.shop.config.RabbitMQConfig;
import fun.timu.cloud.net.shop.controller.request.ConfirmOrderRequest;
import fun.timu.cloud.net.shop.controller.request.ProductOrderPageRequest;
import fun.timu.cloud.net.shop.manager.ProductManager;
import fun.timu.cloud.net.shop.manager.ProductOrderManager;
import fun.timu.cloud.net.shop.mapper.ProductOrderMapper;
import fun.timu.cloud.net.shop.model.DO.Product;
import fun.timu.cloud.net.shop.model.DO.ProductOrder;
import fun.timu.cloud.net.shop.model.VO.PayInfoVO;
import fun.timu.cloud.net.shop.service.ProductOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengke
 * @description 针对表【product_order】的数据库操作Service实现
 * @createDate 2025-04-18 10:28:49
 */
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements ProductOrderService {
    private static Logger logger = LoggerFactory.getLogger(ProductOrderService.class);

    private final ProductOrderManager productOrderManager;
    private final ProductManager productManager;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;
    private final PayFactory payFactory;
    private final RedisTemplate<Object, Object> redisTemplate;


    public ProductOrderServiceImpl(ProductOrderManager productOrderManager, ProductManager productManager, RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig, PayFactory payFactory, RedisTemplate<Object, Object> redisTemplate) {
        this.productOrderManager = productOrderManager;
        this.productManager = productManager;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
        this.payFactory = payFactory;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 根据订单分页请求获取订单列表
     * 此方法主要用于处理订单的分页查询逻辑，根据当前登录用户和请求参数返回相应的订单信息
     *
     * @param orderPageRequest 订单分页请求对象，包含页码、每页大小、用户状态等信息
     * @return 返回一个包含订单信息的Map对象
     */
    @Override

    public Map<String, Object> page(ProductOrderPageRequest orderPageRequest) {
        // 获取当前登录用户的账号编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 调用产品订单管理器的分页方法，传入页码、每页大小、用户账号编号和订单状态，获取分页结果
        Map<String, Object> pageResult = productOrderManager.page(orderPageRequest.getPage(), orderPageRequest.getSize(), accountNo, orderPageRequest.getState());
        // 返回分页结果
        return pageResult;
    }

    /**
     * 根据外部交易号查询产品订单状态
     *
     * @param outTradeNo 外部交易号，用于标识特定的交易
     * @return 返回订单的状态如果找不到对应的订单，则返回空字符串
     */
    @Override
    public String queryProductOrderState(String outTradeNo) {
        // 获取当前登录用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 根据外部交易号和账户编号查询产品订单
        ProductOrder productOrderDO = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo, accountNo);
        // 如果没有找到对应的订单，返回空字符串
        if (productOrderDO == null) {
            return "";
        } else {
            // 否则，返回订单的状态
            return productOrderDO.getState();
        }
    }

    /**
     * 确认订单并创建支付信息
     * <p>
     * 本方法主要用于处理订单确认逻辑，包括生成订单号、验证产品价格、创建订单记录、
     * 以及构建支付信息对象，以供后续支付流程使用
     *
     * @param orderRequest 订单请求对象，包含产品ID、客户端类型、支付类型等信息
     * @return 返回JsonData对象，包含处理结果
     */
    @Override
    @Transactional
    public JsonData confirmOrder(ConfirmOrderRequest orderRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        String orderOutTradeNo = CommonUtil.getStringNumRandom(32);

        Product productDO = productManager.findDetailById(orderRequest.getProductId());

        //验证价格
        this.checkPrice(productDO, orderRequest);

        //创建订单
        ProductOrder productOrderDO = this.saveProductOrder(orderRequest, loginUser, orderOutTradeNo, productDO);

        //创建支付对象
        PayInfoVO payInfoVO = PayInfoVO.builder().accountNo(loginUser.getAccountNo()).outTradeNo(orderOutTradeNo).clientType(orderRequest.getClientType()).payType(orderRequest.getPayType()).title(productDO.getTitle()).description("").payFee(orderRequest.getPayAmount()).orderPayTimeoutMills(TimeConstant.ORDER_PAY_TIMEOUT_MILLS).build();

        //发送延迟消息
        EventMessage eventMessage = EventMessage.builder().eventMessageType(EventMessageType.PRODUCT_ORDER_NEW.name()).accountNo(loginUser.getAccountNo()).bizId(orderOutTradeNo).build();

        rabbitTemplate.convertAndSend(rabbitMQConfig.getOrderEventExchange(), rabbitMQConfig.getOrderCloseDelayRoutingKey(), eventMessage);


        // TODO 调用支付信息
        String codeUrl = payFactory.pay(payInfoVO);
        // 检查支付码URL是否非空
        if (StringUtils.isNotBlank(codeUrl)) {
            // 初始化结果映射，用于存储支付相关的关键信息
            Map<String, String> resultMap = new HashMap<>(2);
            // 将支付码URL放入结果映射中
            resultMap.put("code_url", codeUrl);
            // 将商户订单号放入结果映射中
            resultMap.put("out_trade_no", payInfoVO.getOutTradeNo());
            resultMap.put("pay_type", payInfoVO.getPayType());
            resultMap.put("account_no", payInfoVO.getAccountNo().toString());
            // 返回成功构建的JSON数据，包含支付码URL和商户订单号
            return JsonData.buildSuccess(resultMap);
        }
        return JsonData.buildResult(BizCodeEnum.PAY_ORDER_FAIL);
    }

    /**
     * 关闭产品订单
     * <p>
     * 此方法处理订单关闭请求，主要步骤如下：
     * 1. 根据事件消息中的业务ID和账户号查询订单。
     * 2. 如果订单不存在，则记录警告日志并返回成功。
     * 3. 如果订单已支付，则记录信息日志并返回成功。
     * 4. 如果订单未支付，尝试向第三方支付平台查询支付状态（实际查询逻辑未实现）。
     * 5. 根据查询结果更新订单状态为取消或支付，并执行相应日志记录。
     *
     * @param eventMessage 事件消息，包含业务ID和账户号等信息
     * @return 总是返回true，表示处理成功
     */
    @Override
    public boolean closeProductOrder(EventMessage eventMessage) {

        // 获取事件消息中的业务ID和账户号
        String outTradeNo = eventMessage.getBizId();
        Long accountNo = eventMessage.getAccountNo();

        // 根据业务ID和账户号查询订单
        ProductOrder productOrderDO = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo, accountNo);

        // 如果订单不存在
        if (productOrderDO == null) {
            log.warn("订单不存在");
            return true;
        }

        // 如果订单已经支付
        if (productOrderDO.getState().equalsIgnoreCase(ProductOrderStateEnum.PAY.name())) {
            logger.info("直接确认消息，订单已经支付:{}", eventMessage);
            return true;
        }

        // 未支付，需要向第三方支付平台查询状态
        if (productOrderDO.getState().equalsIgnoreCase(ProductOrderStateEnum.NEW.name())) {
            // 向第三方查询状态
            PayInfoVO payInfoVO = new PayInfoVO();
            payInfoVO.setPayType(productOrderDO.getPayType());
            payInfoVO.setOutTradeNo(outTradeNo);
            payInfoVO.setAccountNo(accountNo);

            // TODO 需要向第三方支付平台查询状态
            String payResult = "";

            // 根据查询结果处理订单状态
            if (StringUtils.isBlank(payResult)) {
                // 如果为空，则未支付成功，本地取消订单
                productOrderManager.updateOrderPayState(outTradeNo, accountNo, ProductOrderStateEnum.CANCEL.name(), ProductOrderStateEnum.NEW.name());
                logger.info("未支付成功，本地取消订单:{}", eventMessage);
            } else {
                // 支付成功，主动把订单状态更新成支付
                logger.warn("支付成功，但是微信回调通知失败，需要排查问题:{}", eventMessage);
                productOrderManager.updateOrderPayState(outTradeNo, accountNo, ProductOrderStateEnum.PAY.name(), ProductOrderStateEnum.NEW.name());
                // TODO 触发支付成功后的逻辑
            }
        }

        return true;
    }

    /**
     * 处理订单回调消息
     *
     * @param payType   支付类型枚举，表示支付方式
     * @param paramsMap 包含回调参数的映射，由支付平台提供
     * @return 返回一个包含处理结果的JsonData对象
     */
    @Override
    public JsonData processOrderCallbackMsg(ProductOrderPayTypeEnum payType, Map<String, String> paramsMap) {
        //获取商户订单号
        String outTradeNo = paramsMap.get("out_trade_no");
        //交易状态
        String tradeState = paramsMap.get("trade_state");

        //账户编号
        Long accountNo = Long.valueOf(paramsMap.get("account_no"));

        //根据商户订单号和账户编号查询订单信息
        ProductOrder productOrderDO = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo, accountNo);

        //创建一个映射用于存储消息内容
        Map<String, Object> content = new HashMap<>(4);
        content.put("outTradeNo", outTradeNo);
        content.put("buyNum", productOrderDO.getBuyNum());
        content.put("accountNo", accountNo);
        content.put("product", productOrderDO.getProductSnapshot());

        //构建消息
        EventMessage eventMessage = EventMessage.builder()
                .bizId(outTradeNo)
                .accountNo(accountNo)
                .messageId(outTradeNo)
                .content(JsonUtil.obj2Json(content))
                .eventMessageType(EventMessageType.PRODUCT_ORDER_PAY.name())
                .build();

        //根据支付类型处理回调消息
        if (payType.name().equalsIgnoreCase(ProductOrderPayTypeEnum.ALI_PAY.name())) {
            //支付宝支付 TODO

        } else if (payType.name().equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT_PAY.name())) {
            //处理微信支付回调
            if ("SUCCESS".equalsIgnoreCase(tradeState)) {
                //如果交易状态为成功

                //设置过期时间
                Boolean flag = redisTemplate.opsForValue().setIfAbsent(outTradeNo, "OK", 3, TimeUnit.DAYS);

                if (flag) {
                    //发送消息到 RabbitMQ
                    rabbitTemplate.convertAndSend(rabbitMQConfig.getOrderEventExchange(), rabbitMQConfig.getOrderUpdateTrafficRoutingKey(), eventMessage);

                    return JsonData.buildSuccess();
                }
            }
        }

        //如果回调处理不成功，返回相应的错误码
        return JsonData.buildResult(BizCodeEnum.PAY_ORDER_CALLBACK_NOT_SUCCESS);
    }

    /**
     * 处理产品订单消息
     * 根据消息类型处理相应的订单事件
     *
     * @param eventMessage 事件消息，包含订单相关信息
     */
    @Override
    public void handleProductOrderMessage(EventMessage eventMessage) {
        // 获取消息类型
        String messageType = eventMessage.getEventMessageType();

        try {
            // 根据消息类型处理订单
            if (EventMessageType.PRODUCT_ORDER_NEW.name().equalsIgnoreCase(messageType)) {
                // 关闭订单
                this.closeProductOrder(eventMessage);

            } else if (EventMessageType.PRODUCT_ORDER_PAY.name().equalsIgnoreCase(messageType)) {
                // 订单已经支付，更新订单状态
                String outTradeNo = eventMessage.getBizId();
                Long accountNo = eventMessage.getAccountNo();
                // 更新订单支付状态，从新建状态转为已支付状态
                int rows = productOrderManager.updateOrderPayState(outTradeNo, accountNo,
                        ProductOrderStateEnum.PAY.name(), ProductOrderStateEnum.NEW.name());
                // 记录订单更新日志
                logger.info("订单更新成功:rows={},eventMessage={}", rows, eventMessage);
            }

        } catch (Exception e) {
            // 记录订单消费失败日志
            logger.error("订单消费者消费失败:{}", eventMessage);
            // 抛出业务异常，指示消息消费失败
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
    }

    /**
     * 检查价格方法
     * 该方法用于验证前端提交的订单总价与后端根据产品信息计算得到的总价是否一致
     * 主要目的是为了防止价格篡改和确保订单的准确性
     *
     * @param productDO    包含产品详细信息的对象，如产品价格等
     * @param orderRequest 包含用户提交的订单信息，如购买数量和支付总价等
     * @throws BizException 如果前后端价格不一致，抛出业务异常，阻止订单创建
     */
    private void checkPrice(Product productDO, ConfirmOrderRequest orderRequest) {

        // 后端计算价格
        // 根据用户购买的数量和产品单价计算总价格，确保计算的准确性
        BigDecimal bizTotal = BigDecimal.valueOf(orderRequest.getBuyNum()).multiply(productDO.getAmount());

        // 前端传递总价和后端计算总价格是否一致, 如果有优惠券，也在这里进行计算
        // 比较前后端的价格是否一致，如果不一致，记录错误日志并抛出异常
        // 这里也考虑了优惠券的使用情况，但具体优惠券的计算逻辑未在代码中体现
        if (bizTotal.compareTo(orderRequest.getPayAmount()) != 0) {
            logger.error("验证价格失败{}", orderRequest);
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_PRICE_FAIL);
        }

    }

    /**
     * 保存产品订单信息
     *
     * @param orderRequest    订单请求对象，包含用户提交的订单信息
     * @param loginUser       当前登录用户信息
     * @param orderOutTradeNo 订单外部交易号，用于唯一标识一笔交易
     * @param productDO       产品信息对象，表示用户购买的产品详情
     * @return 返回保存后的订单对象
     */
    private ProductOrder saveProductOrder(ConfirmOrderRequest orderRequest, LoginUser loginUser, String orderOutTradeNo, Product productDO) {
        // 创建一个新的产品订单对象
        ProductOrder productOrderDO = new ProductOrder();

        //设置用户信息
        productOrderDO.setAccountNo(loginUser.getAccountNo());
        productOrderDO.setNickname(loginUser.getUsername());

        //设置商品信息
        productOrderDO.setProductId(productDO.getId());
        productOrderDO.setProductTitle(productDO.getTitle());
        // 将产品信息序列化为JSON字符串保存，以便后续可能的反序列化或审计需求
        productOrderDO.setProductSnapshot(JsonUtil.obj2Json(productDO));
        productOrderDO.setProductAmount(productDO.getAmount());

        //设置订单信息
        productOrderDO.setBuyNum(orderRequest.getBuyNum());
        productOrderDO.setOutTradeNo(orderOutTradeNo);
        productOrderDO.setCreateTime(new Date());
        productOrderDO.setDel(0);

        //发票信息
        productOrderDO.setBillType(BillTypeEnum.valueOf(orderRequest.getBillType()).name());
        productOrderDO.setBillHeader(orderRequest.getBillHeader());
        productOrderDO.setBillReceiverPhone(orderRequest.getBillReceiverPhone());
        productOrderDO.setBillReceiverEmail(orderRequest.getBillReceiverEmail());
        productOrderDO.setBillContent(orderRequest.getBillContent());

        //实际支付总价
        productOrderDO.setPayAmount(orderRequest.getPayAmount());
        //总价，没使用优惠券
        productOrderDO.setTotalAmount(orderRequest.getTotalAmount());
        //订单状态
        productOrderDO.setState(ProductOrderStateEnum.NEW.name());
        //支付类型
        productOrderDO.setPayType(ProductOrderPayTypeEnum.valueOf(orderRequest.getPayType()).name());

        //将订单信息插入数据库
        productOrderManager.add(productOrderDO);

        //返回保存后的订单对象
        return productOrderDO;
    }
}




