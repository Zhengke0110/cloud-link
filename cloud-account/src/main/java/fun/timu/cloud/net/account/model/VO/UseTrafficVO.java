package fun.timu.cloud.net.account.model.VO;


import fun.timu.cloud.net.account.model.DO.Traffic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UseTrafficVO {


    /**
     * 天剩余可用总次数 = 总次数 - 已用
     */
    private Integer dayTotalLeftTimes;


    /**
     * 当前使用的流量包
     */
    private Traffic currentTrafficDO;


    /**
     * 记录没过期，但是今天没更新的流量包id
     */
    private List<Long> unUpdatedTrafficIds;


}