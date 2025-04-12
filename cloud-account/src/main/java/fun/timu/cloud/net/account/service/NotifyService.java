package fun.timu.cloud.net.account.service;

import fun.timu.cloud.net.enums.SendCodeEnum;
import fun.timu.cloud.net.util.JsonData;

public interface NotifyService {
    JsonData sendCode(SendCodeEnum sendCodeEnum, String to);
}
