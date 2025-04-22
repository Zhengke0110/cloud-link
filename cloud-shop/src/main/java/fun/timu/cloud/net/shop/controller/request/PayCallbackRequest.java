package fun.timu.cloud.net.shop.controller.request;

import lombok.Data;

@Data
public class PayCallbackRequest {
    private String outTradeNo;
    private String tradeState;
    private String accountNo;
}
