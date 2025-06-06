package fun.timu.cloud.net.link.controller.request;

import lombok.Data;

@Data
public class ShortLinkUpdateRequest {
    /**
     * 短链码
     */
    private String code;

    /**
     * 组
     */
    private Long groupId;

    /**
     * 标题
     */
    private String title;

    /**
     * 域名id
     */
    private Long domainId;

    /**
     * 域名类型
     */
    private String domainType;

}
