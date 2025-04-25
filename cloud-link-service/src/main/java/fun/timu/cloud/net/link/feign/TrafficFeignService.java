package fun.timu.cloud.net.link.feign;

import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.link.controller.request.UseTrafficRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cloud-account-service")
public interface TrafficFeignService {

    /**
     * 使用流量包
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/api/traffic/v1/reduce", headers = {"rpc-token=${rpc.token}"})
    JsonData useTraffic(@RequestBody UseTrafficRequest request);

}
