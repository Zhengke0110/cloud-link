package fun.timu.cloud.net.link.controller;

import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.link.controller.request.ShortLinkAddRequest;
import fun.timu.cloud.net.link.controller.request.ShortLinkDelRequest;
import fun.timu.cloud.net.link.controller.request.ShortLinkPageRequest;
import fun.timu.cloud.net.link.controller.request.ShortLinkUpdateRequest;
import fun.timu.cloud.net.link.model.VO.ShortLinkVO;
import fun.timu.cloud.net.link.service.ShortLinkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 短链接控制器
 * 该控制器处理与短链接相关的请求，包括创建、检查、分页查询和删除短链接等操作
 */
@RestController
@RequestMapping("/api/link/v1")
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    public ShortLinkController(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }


    @Value("${rpc.token}")
    private String rpcToken;

    /**
     * 检查短链的有效性
     *
     * @param shortLinkCode 短链代码，用于解析短链信息
     * @param request       HTTP请求对象，用于获取请求头信息
     * @return 返回一个JsonData对象，包含检查结果
     */
    @GetMapping("check")
    public JsonData check(@RequestParam("shortLinkCode") String shortLinkCode, HttpServletRequest request) {

        // 从请求头中获取token
        String token = request.getHeader("rpc-token");

        // 检查token是否匹配，以验证请求的合法性
        if (rpcToken.equalsIgnoreCase(token)) {

            // 解析短链代码，获取短链信息
            ShortLinkVO shortLinkVO = shortLinkService.parseShortLinkCode(shortLinkCode);
            if (shortLinkVO == null) {
                return JsonData.buildError("短链不存在");
            }
            // ✅ 新增：检查是否过期
            if (shortLinkVO.getExpired() != null && shortLinkVO.getExpired().before(new Date())) {
                return JsonData.buildError("短链接已过期");
            }
            return JsonData.buildSuccess(shortLinkVO);
        } else {
            // 如果token不匹配，返回非法访问错误
            return JsonData.buildError("非法访问");
        }
    }


    /**
     * 创建短链接的控制器方法
     * 该方法接收一个HTTP POST请求，用于添加新的短链接信息
     *
     * @param request 包含短链接相关数据的请求对象，由请求体自动转换而来
     * @return 返回一个JsonData对象，包含处理结果和可能的数据
     */
    @PostMapping("add")
    public JsonData createShortLink(@RequestBody ShortLinkAddRequest request) {

        // 调用服务层方法创建短链接
        JsonData jsonData = shortLinkService.createShortLink(request);

        // 返回处理结果
        return jsonData;
    }

    /**
     * 根据组ID分页查询短链接信息
     * 此方法使用@RequestMapping注解来映射HTTP请求到此方法
     * 请求体应包含ShortLinkPageRequest对象，其中包含分页查询所需的信息
     *
     * @param request ShortLinkPageRequest对象，包含分页查询参数
     * @return 返回一个JsonData对象，其中包含分页查询结果
     */
    @RequestMapping("page")
    public JsonData pageByGroupId(@RequestBody ShortLinkPageRequest request) {
        // 调用shortLinkService的pageByGroupId方法执行分页查询
        Map<String, Object> result = shortLinkService.pageByGroupId(request);

        // 构建并返回包含查询结果的JsonData对象
        return JsonData.buildSuccess(result);
    }


    /**
     * 删除短链
     *
     * @param request
     * @return
     */
    @PostMapping("del")
    public JsonData del(@RequestBody ShortLinkDelRequest request) {
        // 调用shortLinkService的del方法删除短链
        JsonData jsonData = shortLinkService.del(request);

        // 返回删除结果
        return jsonData;
    }


    /**
     * 更新短链
     *
     * @param request
     * @return
     */
    @PostMapping("update")
    public JsonData update(@RequestBody ShortLinkUpdateRequest request) {

        JsonData jsonData = shortLinkService.update(request);

        return jsonData;
    }

    /**
     * 查询短链创建任务状态
     *
     * @param taskId 要查询的任务ID
     * @return 返回任务当前的状态（PENDING / SUCCESS / FAILED）
     */
    @GetMapping("query-task/{taskId}")
    public JsonData queryTaskStatus(@PathVariable String taskId) {
        return shortLinkService.queryTaskStatus(taskId);
    }
}