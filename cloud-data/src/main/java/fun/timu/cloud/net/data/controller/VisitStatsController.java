package fun.timu.cloud.net.data.controller;

import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.data.controller.request.VisitRecordPageRequest;
import fun.timu.cloud.net.data.service.VisitStatsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
}
