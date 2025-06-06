package fun.timu.cloud.net.app.model.DO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkVisitStats {
    /**
     * 窗口开始时间  Clickhouse里面会用到
     */
    String startTime;

    /**
     * 窗口结束时间
     */
    String endTime;

/**
 * ======================================================
 * 以上为时间窗口字段，用于Clickhouse数据库中定义数据的时间范围
 * ======================================================
 */

    /**
     * 短链压缩码
     */
    private String code;

    /**
     * 租户id
     */
    private Long accountNo;

    /**
     * 访问时间
     */
    private Long visitTime;

    /**
     * 站点来源，只记录域名
     */
    private String referer;

    /**
     * 1是新访客，0是老访客
     */
    private Integer isNew;

    /**
     * 唯一标识
     */
    private String udid;

    //==============RegionInfoDO==================

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 运营商
     */
    private String isp;

    /**
     * 访问ip
     */
    private String ip;

//==============DeviceInfoDO==================

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 系统版本
     */
    private String osVersion;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备厂商
     */
    private String deviceManufacturer;

//==============度量==================

    /**
     * 累计独立访客数
     */
    private Long uv = 0L;

    /**
     * 累计访问次数
     */
    private Long pv = 0L;
}

