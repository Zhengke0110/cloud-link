package fun.timu.cloud.net.data.controller;

import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.data.controller.request.*;
import fun.timu.cloud.net.data.model.VO.VisitStatsVO;
import fun.timu.cloud.net.data.service.VisitStatsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 访问统计控制器
 */
@RestController
@RequestMapping("/api/visitStats/v1")
public class VisitStatsController {
    private final VisitStatsService statsService;

    public VisitStatsController(VisitStatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * 处理页面访问记录的请求
     * <p>
     * 此方法用于处理分页查询访问记录的请求它首先检查请求的总条数是否超过限制，
     * 如果超过，则返回错误信息；否则，调用服务层方法获取分页数据并返回
     *
     * @param request 包含分页查询信息的请求对象
     * @return 包含分页数据或错误信息的JsonData对象
     */
    @RequestMapping("pageRecord")
    public JsonData pageVisitRecord(@RequestBody VisitRecordPageRequest request) {

        // 检查请求的总条数是否超过限制
        int total = request.getSize() * request.getPage();
        if (total > 1000) {
            return JsonData.buildResult(BizCodeEnum.DATA_OUT_OF_LIMIT_SIZE);
        }

        // 调用服务层方法获取分页数据
        Map<String, Object> pageResult = statsService.pageVisitRecord(request);

        // 返回成功响应，包含分页数据
        return JsonData.buildSuccess(pageResult);
    }


    /**
     * 查询时间范围内的，地区访问分布
     * <p>
     * 该方法通过接收一个请求对象来查询特定时间范围内不同地区的访问分布情况
     * 它使用了@RequestMapping注解来映射HTTP请求到此方法，并使用@RequestBody注解来将HTTP请求的正文转换为RegionQueryRequest对象
     *
     * @param request 包含查询条件的请求对象，如开始时间、结束时间等
     * @return 返回一个JsonData对象，其中包含查询到的地区访问分布信息
     */
    @RequestMapping("regionDay")
    public JsonData queryRegionWithDay(@RequestBody RegionQueryRequest request) {

        // 调用statsService的queryRegionWithDay方法来处理查询请求，并获取结果列表
        List<VisitStatsVO> list = statsService.queryRegionWithDay(request);

        // 使用JsonData的buildSuccess静态方法构建一个成功的响应对象，其中包含查询结果列表
        return JsonData.buildSuccess(list);
    }


    /**
     * 访问趋势图
     * <p>
     * 该方法用于处理查询访问趋势的请求它接收一个VisitTrendQueryRequest对象作为参数，
     * 该对象包含了查询访问趋势所需的参数信息方法通过调用statsService的queryVisitTrend方法
     * 获取访问趋势数据，并将结果封装在JsonData对象中返回
     *
     * @param request 包含查询参数的VisitTrendQueryRequest对象
     * @return 包含访问趋势数据的JsonData对象
     */
    @RequestMapping("trend")
    public JsonData queryVisitTrend(@RequestBody VisitTrendQueryRequest request) {

        // 调用服务层方法查询访问趋势数据
        List<VisitStatsVO> list = statsService.queryVisitTrend(request);
        // 构建并返回成功的结果对象
        return JsonData.buildSuccess(list);

    }


    /**
     * 高频来源统计接口
     * 该接口用于获取系统中频繁访问的来源信息，通过分析访问数据来确定主要的流量来源
     * 对于参数和返回值的详细说明，采用JSON格式进行数据交互，以适应前后端分离的架构设计
     *
     * @param request 包含查询条件的请求对象，通过RequestBody注解接收前端传来的JSON数据
     * @return 返回一个JsonData对象，其中包含查询结果，该对象用于统一前后端数据交互的格式
     */
    @RequestMapping("frequentSource")
    public JsonData queryFrequentSource(@RequestBody FrequentSourceRequset request) {

        // 调用statsService的queryFrequentSource方法，传入请求参数，获取高频来源统计数据列表
        List<VisitStatsVO> list = statsService.queryFrequentSource(request);
        // 使用JsonData的buildSuccess静态方法构建一个成功的响应对象，将查询结果列表作为参数传入
        return JsonData.buildSuccess(list);
    }


    /**
     * 查询设备访问分布情况
     * <p>
     * 该方法通过接收一个QueryDeviceRequest对象作为参数，来查询设备的访问分布情况
     * 它使用了@RequestMapping注解来映射HTTP请求到此方法，参数通过@RequestBody注解指定，意味着请求体中的内容将被转换为方法参数
     *
     * @param request 包含查询设备访问分布所需信息的请求对象
     * @return 返回一个JsonData对象，其中包含查询结果，结果是一个Map，键为字符串类型，值为VisitStatsVO对象列表
     */
    @RequestMapping("deviceInfo")
    public JsonData queryDeviceInfo(@RequestBody QueryDeviceRequest request) {

        // 调用statsService的queryDeviceInfo方法来处理查询请求，并获取设备访问分布信息
        Map<String, List<VisitStatsVO>> map = statsService.queryDeviceInfo(request);
        // 使用JsonData的buildSuccess静态方法构建一个成功的响应对象，包含查询到的设备访问分布信息
        return JsonData.buildSuccess(map);
    }

}
