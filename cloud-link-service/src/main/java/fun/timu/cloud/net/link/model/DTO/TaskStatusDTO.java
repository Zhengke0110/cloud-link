package fun.timu.cloud.net.link.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDTO {
    private String status;
    /**
     * 组
     */
    private Long groupId;

    /**
     * 短链标题
     */
    private String title;

    /**
     * 原始url地址
     */
    private String originalUrl;

    /**
     * 短链域名
     */
    private String domain;

    /**
     * 短链压缩码
     */
    private String code;


    /**
     * 过期时间，长久就是-1
     */
    private Date expired;

    /**
     * 账号唯一编号
     */
    private Long accountNo;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 0是默认，1是删除
     */
    private Integer del;

    /**
     * 状态，lock是锁定不可用，active是可用
     */
    private String state;

    /**
     * 链接产品层级：FIRST 免费青铜、SECOND黄金、THIRD钻石
     */
    private String linkType;
}
