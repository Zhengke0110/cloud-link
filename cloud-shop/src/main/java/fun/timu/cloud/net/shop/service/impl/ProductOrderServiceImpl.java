package fun.timu.cloud.net.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import fun.timu.cloud.net.common.constant.TimeConstant;
import fun.timu.cloud.net.common.enums.BillTypeEnum;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.enums.ProductOrderPayTypeEnum;
import fun.timu.cloud.net.common.enums.ProductOrderStateEnum;
import fun.timu.cloud.net.common.exception.BizException;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.common.model.LoginUser;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.common.util.JsonUtil;
import fun.timu.cloud.net.shop.controller.request.ConfirmOrderRequest;
import fun.timu.cloud.net.shop.manager.ProductManager;
import fun.timu.cloud.net.shop.manager.ProductOrderManager;
import fun.timu.cloud.net.shop.mapper.ProductOrderMapper;
import fun.timu.cloud.net.shop.model.DO.Product;
import fun.timu.cloud.net.shop.model.DO.ProductOrder;
import fun.timu.cloud.net.shop.model.VO.PayInfoVO;
import fun.timu.cloud.net.shop.service.ProductOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

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

    public ProductOrderServiceImpl(ProductOrderManager productOrderManager, ProductManager productManager) {
        this.productOrderManager = productOrderManager;
        this.productManager = productManager;
    }

    /**
     * 根据页面编号、页面大小和订单状态分页查询订单
     * 此方法利用拦截器中的登录信息来获取账户编号，并根据给定的页面参数和订单状态，
     * 调用产品订单管理器的分页方法来获取相应的订单信息
     *
     * @param page  页面编号，表示请求的页面位置
     * @param size  页面大小，表示每页包含的订单数量
     * @param state 订单状态，用于过滤特定状态的订单，如果为null则不进行状态过滤
     * @return 返回一个包含分页订单信息的映射对象，其中包含了订单列表和分页细节
     */
    @Override
    public Map<String, Object> page(int page, int size, String state) {
        // 从登录拦截器的线程局部变量中获取当前登录账户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 调用产品订单管理器的分页方法，传入页面编号、页面大小、账户编号和订单状态，
        // 获取分页后的订单信息映射对象
        Map<String, Object> pageResult = productOrderManager.page(page, size, accountNo, state);
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
     *
     * 本方法主要用于处理订单确认逻辑，包括生成订单号、验证产品价格、创建订单记录、
     * 以及构建支付信息对象，以供后续支付流程使用
     *
     * @param orderRequest 订单请求对象，包含产品ID、客户端类型、支付类型等信息
     * @return 返回JsonData对象，包含处理结果
     */
    @Override
    @Transactional
    public JsonData confirmOrder(ConfirmOrderRequest orderRequest) {
        // 获取当前登录用户信息
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        // 生成订单号
        String orderOutTradeNo = CommonUtil.getStringNumRandom(32);

        // 根据产品ID查询产品详细信息
        Product productDO = productManager.findDetailById(orderRequest.getProductId());

        // 验证产品价格是否与请求中的支付金额一致
        this.checkPrice(productDO, orderRequest);

        // 创建订单记录并保存到数据库
        ProductOrder productOrderDO = this.saveProductOrder(orderRequest, loginUser, orderOutTradeNo, productDO);

        // 创建支付信息对象，用于后续的支付流程
        PayInfoVO payInfoVO = PayInfoVO.builder().accountNo(loginUser.getAccountNo())
                .outTradeNo(orderOutTradeNo).clientType(orderRequest.getClientType())
                .payType(orderRequest.getPayType()).title(productDO.getTitle()).description("")
                .payFee(orderRequest.getPayAmount()).orderPayTimeoutMills(TimeConstant.ORDER_PAY_TIMEOUT_MILLS)
                .build();

        // 发送延迟消息，用于订单支付超时未支付的处理 TODO

        // 调用支付接口，进行实际的支付流程 TODO

        // 返回处理结果
        return null;
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




