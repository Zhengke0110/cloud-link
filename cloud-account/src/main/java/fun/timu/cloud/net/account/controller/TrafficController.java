package fun.timu.cloud.net.account.controller;

import fun.timu.cloud.net.account.controller.request.TrafficPageRequest;
import fun.timu.cloud.net.account.controller.request.UseTrafficRequest;
import fun.timu.cloud.net.account.model.VO.TrafficVO;
import fun.timu.cloud.net.account.service.TrafficService;
import fun.timu.cloud.net.common.util.JsonData;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 流量包控制器接口
 */
@RestController
@RequestMapping("/api/traffic/v1")
public class TrafficController {
    private final TrafficService trafficService;

    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    /**
     * 使用流量包接口
     * 该接口接收一个UseTrafficRequest对象作为请求体，处理用户使用流量包的请求
     * 主要用于减少用户账户中的流量余额
     *
     * @param useTrafficRequest 包含用户ID和所需减少的流量信息的请求对象
     * @param request           HTTP请求对象，用于获取请求相关的上下文信息
     * @return 返回一个JsonData对象，包含处理结果
     * 目前仅作为成功处理的标志返回，实际使用时可能包含更详细的信息
     */
    @PostMapping("reduce")
    public JsonData useTraffic(@RequestBody UseTrafficRequest useTrafficRequest, HttpServletRequest request) {

        //具体使用流量包逻辑  TODO

        return JsonData.buildSuccess();
    }

    /**
     * 分页查询流量包列表，查看可用的流量包
     *
     * @param request
     * @return
     */
    @RequestMapping("page")
    public JsonData pageAvailable(@RequestBody TrafficPageRequest request) {
        // 根据请求参数分页查询可用的流量包
        Map<String, Object> pageMap = trafficService.pageAvailable(request);

        // 构建并返回查询结果的JSON数据
        return JsonData.buildSuccess(pageMap);

    }


    /**
     * 查找某个流量包详情
     *
     * @param trafficId 流量包的唯一标识符
     * @return 返回包含流量包详情的Json数据
     */
    @GetMapping("/detail/{trafficId}")
    public JsonData detail(@PathVariable("trafficId") long trafficId) {

        // 调用服务层方法获取流量包详情
        TrafficVO trafficVO = trafficService.detail(trafficId);

        // 构建并返回成功的JSON响应，包含流量包详情
        return JsonData.buildSuccess(trafficVO);
    }

}
