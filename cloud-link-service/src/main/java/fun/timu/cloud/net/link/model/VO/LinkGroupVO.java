package fun.timu.cloud.net.link.model.VO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * LinkGroupVO类代表一个链接分组的视图对象
 * 它用于在不同的层之间传递链接分组的相关信息
 * 实现了Serializable接口，以支持对象的序列化和反序列化
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LinkGroupVO implements Serializable {

    /**
     * 链接分组的唯一标识符
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 组名
     */
    private String title;

    /**
     * 账号唯一编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountNo;

    /**
     * 记录创建时间
     */
    private Date gmtCreate;

    /**
     * 记录最后修改时间
     */
    private Date gmtModified;
}
