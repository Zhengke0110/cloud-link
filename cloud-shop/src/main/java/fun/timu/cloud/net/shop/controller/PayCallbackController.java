package fun.timu.cloud.net.shop.controller;

import fun.timu.cloud.net.common.enums.ProductOrderPayTypeEnum;
import fun.timu.cloud.net.shop.controller.request.ConfirmOrderRequest;
import fun.timu.cloud.net.shop.controller.request.PayCallbackRequest;
import fun.timu.cloud.net.shop.service.ProductOrderService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付相关控制器接口
 */
@RestController
@RequestMapping("/api/callback/order/v1/")
public class PayCallbackController {
    private final ProductOrderService productOrderService;

    public PayCallbackController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @PostMapping("wechat")
    @ResponseBody
    public void WechatPayCallback(@RequestBody PayCallbackRequest payCallbackRequest, HttpServletResponse response) {


        Map<String, String> paramsMap =  new ConcurrentHashMap<>();
        paramsMap.put("trade_state", payCallbackRequest.getTradeState());
        paramsMap.put("out_trade_no", payCallbackRequest.getOutTradeNo());
        paramsMap.put("account_no", payCallbackRequest.getAccountNo());
        productOrderService.processOrderCallbackMsg(ProductOrderPayTypeEnum.WECHAT_PAY, paramsMap);
    }
}