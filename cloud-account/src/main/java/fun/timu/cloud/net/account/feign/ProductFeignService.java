package fun.timu.cloud.net.account.feign;

import fun.timu.cloud.net.common.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dcloud-shop-service")
public interface ProductFeignService {


    /**
     * 获取流量包商品详情
     *
     * @param productId
     * @return
     */
    @GetMapping("/api/product/v1/detail/{productId}")
    JsonData detail(@PathVariable("productId") long productId);

}
