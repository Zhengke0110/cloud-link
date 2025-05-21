package fun.timu.cloud.net.link.controller.request;

import lombok.Data;

@Data
public class ShortLinkDelRequest {


    /**
     * 组
     */
    private Long groupId;

    /**
     * 短链码
     */
    private String code;

}