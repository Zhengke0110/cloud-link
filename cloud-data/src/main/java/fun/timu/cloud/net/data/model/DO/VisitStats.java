package fun.timu.cloud.net.data.model.DO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName visit_stats
 */
@TableName(value = "visit_stats")
@Data
public class VisitStats implements Serializable {
    /**
     *
     */
    private Object code;

    /**
     *
     */
    private Object referer;

    /**
     *
     */
    private Integer isNew;

    /**
     *
     */
    private Integer accountNo;

    /**
     *
     */
    private Object province;

    /**
     *
     */
    private Object city;

    /**
     *
     */
    private Object ip;

    /**
     *
     */
    private Object browserName;

    /**
     *
     */
    private Object os;

    /**
     *
     */
    private Object deviceType;

    /**
     *
     */
    private Integer pv;

    /**
     *
     */
    private Integer uv;

    /**
     *
     */
    private Date startTime;

    /**
     *
     */
    private Date endTime;

    /**
     *
     */
    private Integer ts;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}