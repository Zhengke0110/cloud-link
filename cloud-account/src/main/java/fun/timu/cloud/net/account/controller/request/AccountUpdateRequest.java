package fun.timu.cloud.net.account.controller.request;


import lombok.Data;

/**
 * 用户信息更新请求类
 */
@Data
public class AccountUpdateRequest {
    /**
     * 头像
     */
    private String headImg;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 用户名
     */
    private String username;
}