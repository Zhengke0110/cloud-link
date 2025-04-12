package fun.timu.cloud.net.account.controller.request;

import lombok.Data;

@Data
public class SendCodeRequest {
    // 验证码
    private String captcha;
    // 手机号/邮箱号
    private String to;
}

