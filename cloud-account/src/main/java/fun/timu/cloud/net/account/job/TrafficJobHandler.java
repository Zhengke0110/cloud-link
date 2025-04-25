package fun.timu.cloud.net.account.job;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import fun.timu.cloud.net.account.service.TrafficService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrafficJobHandler {

    private final TrafficService trafficService;

    public TrafficJobHandler(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    /**
     * 过期流量包处理
     * <p>
     * 本方法被定时任务触发，用于处理过期的流量包
     * 它通过调用trafficService的deleteExpireTraffic方法来删除过期的流量包
     *
     * @param param 定时任务执行时传入的参数，对于此方法来说，参数未使用
     * @return 返回定时任务执行的结果，成功则返回ReturnT.SUCCESS
     */
    @XxlJob(value = "trafficExpiredHandler", init = "init", destroy = "destroy")
    public ReturnT<String> execute(String param) {

        // 记录日志，表示任务方法触发成功，并准备删除过期流量包
        log.info("Account execute 任务方法触发成功,删除过期流量包");
        // 调用trafficService的deleteExpireTraffic方法，处理过期的流量包
        trafficService.deleteExpireTraffic();

        // 返回任务执行成功的结果
        return ReturnT.SUCCESS;
    }

    private void init() {

        log.info("Account MyJobHandler init >>>>>");
    }

    private void destroy() {
        log.info("Account MyJobHandler destroy >>>>>");
    }

}
