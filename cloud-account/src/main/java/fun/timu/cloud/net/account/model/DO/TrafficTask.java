package fun.timu.cloud.net.account.model.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName traffic_task
 */
@TableName(value = "traffic_task")
@Data
public class TrafficTask implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long accountNo;

    /**
     *
     */
    private Long trafficId;

    /**
     *
     */
    private Integer useTimes;

    /**
     * 锁定状态锁定LOCK  完成FINISH-取消CANCEL
     */
    private String lockState;

    /**
     * 唯一标识
     */
    private String bizId;

    /**
     *
     */
    private Date gmtCreate;

    /**
     *
     */
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}