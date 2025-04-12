package fun.timu.cloud.net.account.controller.request;

import lombok.Data;

@Data
public class SendCodeRequest {
    private String captcha;
    private String to;
}

